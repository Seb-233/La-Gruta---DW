package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Adicional;
import com.example.demo.model.Comida;
import com.example.demo.model.Domiciliario;
import com.example.demo.model.Pedido;
import com.example.demo.model.PedidoComida;
import com.example.demo.model.User;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.PedidoComidaRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UserRepository;

/**
 * Controlador REST para flujo de pedidos:
 * - GET /api/pedidos : listar pedidos activos
 * - PUT /api/pedidos/{id}/estado : actualizar estado
 * - POST /api/pedidos : crear pedido desde el carro
 * - GET /api/pedidos/{id} : consultar pedido por id
 * - GET /api/pedidos/carrito/{userId} : consultar carrito activo de un usuario
 * - GET /api/pedidos/todos : listar todos
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoRestController {

    // ===== Repositorios =====
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private DomiciliarioRepository domiciliarioRepository;
    @Autowired
    private PedidoComidaRepository pedidoComidaRepository;
    @Autowired
    private ComidaRepository comidaRepository;
    @Autowired
    private AdicionalRepository adicionalRepository;
    @Autowired
    private UserRepository userRepository;

    // =========================
    // DTOs internos mínimos
    // =========================
    public static class CreatePedidoRequest {
        public Long userId;       // ID del usuario que realiza el pedido
        public String direccion;  // opcional
        public String notas;      // opcional
        public List<Item> items;

        public static class Item {
            public Long comidaId;
            public int cantidad;
            public List<Long> adicionalIds;
        }
    }

    public static class PedidoResponse {
        public Long id;
        public String estado;
        public Double total;
        public LocalDateTime creadoEn;
        public List<Item> items;

        public static class Item {
            public Long pedidoComidaId;
            public Long comidaId;
            public String comidaNombre;
            public int cantidad;
            public List<AdicionalRes> adicionales;
            public Double subtotal;
        }

        public static class AdicionalRes {
            public Long id;
            public String nombre;
            public Double precio;
        }
    }

    // =========================
    // GET: Pedidos activos (no entregados)
    // =========================
    @GetMapping
    public List<Pedido> getPedidosActivos() {
        return pedidoRepository.findAllActive();
    }

    // =========================
    // PUT: Actualizar estado
    // =========================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");

        return pedidoRepository.findById(id).map(pedido -> {
            switch (nuevoEstado) {
                case "cocinando":
                    pedido.setEstado("cocinando");
                    break;

                case "enviado":
                    // Buscar un domiciliario disponible
                    Domiciliario domiciliario = domiciliarioRepository.findAll().stream()
                            .filter(Domiciliario::isDisponible)
                            .filter(dom -> {
                                // Verificar que no tenga ya un pedido activo
                                return !pedidoRepository.existsByDomiciliarioAsignadoAndEstadoIn(
                                        dom,
                                        List.of("enviado", "cocinando", "recibido"));
                            })
                            .findFirst()
                            .orElse(null);

                    if (domiciliario != null) {
                        domiciliario.setDisponible(false);
                        domiciliarioRepository.save(domiciliario);
                        pedido.setEstado("enviado");
                        pedido.setDomiciliarioAsignado(domiciliario);
                    } else {
                        return ResponseEntity.badRequest()
                                .body("No hay domiciliarios disponibles o todos ya tienen pedidos activos");
                    }
                    break;

                case "entregado":
                    pedido.setEstado("entregado");
                    pedido.setFechaEntrega(LocalDateTime.now());

                    if (pedido.getDomiciliarioAsignado() != null) {
                        Domiciliario domAsig = pedido.getDomiciliarioAsignado();
                        domAsig.setDisponible(true);
                        domiciliarioRepository.save(domAsig);
                    }
                    break;

                default:
                    return ResponseEntity.badRequest().body("Estado no soportado: " + nuevoEstado);
            }

            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoActualizado);

        }).orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // POST: Crear/actualizar pedido (carrito)
    // =========================
    @PostMapping
    @Transactional
    public ResponseEntity<?> crearPedido(@RequestBody CreatePedidoRequest body) {
        if (body == null || body.items == null || body.items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El pedido no tiene items"));
        }

        // Validar usuario
        if (body.userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta el userId en el pedido"));
        }

        User user = userRepository.findById(body.userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + body.userId));

        // Verificar si ya tiene un pedido activo (EN_PROCESO)
        Optional<Pedido> pedidoExistente = pedidoRepository.findByUserAndEstado(user, "EN_PROCESO");

        Pedido pedido;
        if (pedidoExistente.isPresent()) {
            // Reutilizar el pedido como carrito activo: eliminar líneas previas correctamente
            pedido = pedidoExistente.get();

            // Si NO tienes orphanRemoval=true, borra explícitamente desde BD antes de limpiar
            List<PedidoComida> oldItems = new ArrayList<>(pedido.getItems());
            pedido.getItems().clear();
            if (!oldItems.isEmpty()) {
                pedidoComidaRepository.deleteAll(oldItems);
            }

        } else {
            // Crear un nuevo pedido/carrito
            pedido = new Pedido();
            pedido.setEstado("EN_PROCESO");
            pedido.setCreadoEn(LocalDateTime.now());
            pedido.setUser(user); // asociación directa
        }

        // Construir los ítems del pedido y calcular total
        double total = 0.0;
        List<PedidoComida> items = new ArrayList<>();

        for (CreatePedidoRequest.Item it : body.items) {
            if (it.comidaId == null || it.cantidad <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item inválido en pedido"));
            }

            Comida comida = comidaRepository.findById(it.comidaId)
                    .orElseThrow(() -> new IllegalArgumentException("Comida inexistente: " + it.comidaId));

            PedidoComida pc = new PedidoComida();
            pc.setPedido(pedido);
            pc.setComida(comida);
            pc.setCantidad(it.cantidad);

            List<Adicional> ads = Collections.emptyList();
            if (it.adicionalIds != null && !it.adicionalIds.isEmpty()) {
                ads = adicionalRepository.findAllById(it.adicionalIds);
            }
            pc.setAdicionales(ads);

            double precioComida = comida.getPrecio();
            double precioAdicionales = 0.0;

            // ✅ Sumar precios de adicionales si existen
            for (Adicional ad : ads) {
                precioAdicionales += ad.getPrecio() != null ? ad.getPrecio() : 0.0;
            }

            total += (precioComida + precioAdicionales) * it.cantidad;
            items.add(pc);
        }

        pedido.setItems(items);
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(mapPedidoToResponse(guardado));
    }

    // =========================
    // GET: Obtener pedido por ID (DTO)
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> getPedido(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(p -> ResponseEntity.ok(mapPedidoToResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // GET: Obtener carrito activo de un usuario (DTO)
    // =========================
    @GetMapping("/carrito/{userId}")
    public ResponseEntity<?> getCarritoUsuario(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }

        Optional<Pedido> pedido = pedidoRepository.findByUserAndEstado(u.get(), "EN_PROCESO");
        if (pedido.isPresent()) {
            return ResponseEntity.ok(mapPedidoToResponse(pedido.get()));
        } else {
            return ResponseEntity.ok(Map.of("mensaje", "El usuario no tiene carrito activo"));
        }
    }

    // =========================
    // GET: Pedidos por usuario (todos los estados)
    // =========================
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<?> getPedidosPorUsuario(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        List<Pedido> pedidos = pedidoRepository.findByUser(u.get());
        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // Mapper entidad -> DTO
    // =========================
    private PedidoResponse mapPedidoToResponse(Pedido p) {
        PedidoResponse r = new PedidoResponse();
        r.id = p.getId();
        r.estado = p.getEstado();
        r.total = p.getTotal();
        r.creadoEn = p.getCreadoEn();

        List<PedidoResponse.Item> list = new ArrayList<>();
        if (p.getItems() != null) {
            for (PedidoComida it : p.getItems()) {
                PedidoResponse.Item ir = new PedidoResponse.Item();
                ir.pedidoComidaId = it.getId();
                ir.comidaId = it.getComida().getId();
                ir.comidaNombre = it.getComida().getNombre();
                ir.cantidad = it.getCantidad();

                // Adicionales con precio en respuesta
                List<PedidoResponse.AdicionalRes> adrs = new ArrayList<>();
                double precioAdicionales = 0.0;
                if (it.getAdicionales() != null) {
                    for (Adicional ad : it.getAdicionales()) {
                        PedidoResponse.AdicionalRes ar = new PedidoResponse.AdicionalRes();
                        ar.id = ad.getId();
                        ar.nombre = ad.getNombre();
                        ar.precio = ad.getPrecio();
                        adrs.add(ar);

                        precioAdicionales += ad.getPrecio() != null ? ad.getPrecio() : 0.0;
                    }
                }
                ir.adicionales = adrs;

                double precioComida = it.getComida().getPrecio();
                ir.subtotal = (precioComida + precioAdicionales) * it.getCantidad();
                list.add(ir);
            }
        }
        r.items = list;
        return r;
    }

    // =========================
    // GET: Todos los pedidos (incluye entregados)
    // =========================
    @GetMapping("/todos")
    public List<Pedido> getTodosLosPedidos() {
        return pedidoRepository.findAll();
    }
}
