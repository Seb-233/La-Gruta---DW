package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.example.demo.dto.CreatePedidoRequest;
import com.example.demo.dto.PedidoResponse;
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

@RestController
@RequestMapping(value = "/api/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // ✅ Permite Angular local sin tocar configuración global
public class PedidoRestController {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private DomiciliarioRepository domiciliarioRepository;
    @Autowired private PedidoComidaRepository pedidoComidaRepository;
    @Autowired private ComidaRepository comidaRepository;
    @Autowired private AdicionalRepository adicionalRepository;
    @Autowired private UserRepository userRepository;

    // =========================
    // 🔹 GET: Pedidos activos (admin)
    // =========================
    @GetMapping
    public List<Pedido> getPedidosActivos() {
        return pedidoRepository.findAllActive();
    }

    // =========================
    // 🔹 PUT: Actualizar estado (admin)
    // =========================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");

        return pedidoRepository.findById(id).map(pedido -> {
            switch (nuevoEstado) {
                case "cocinando" -> pedido.setEstado("cocinando");
                case "enviado" -> {
                    Domiciliario domiciliario = domiciliarioRepository.findAll().stream()
                            .filter(Domiciliario::isDisponible)
                            .filter(dom -> !pedidoRepository.existsByDomiciliarioAsignadoAndEstadoIn(
                                    dom, List.of("enviado", "cocinando", "recibido")))
                            .findFirst().orElse(null);

                    if (domiciliario == null) {
                        return ResponseEntity.badRequest()
                                .body("No hay domiciliarios disponibles o todos ya tienen pedidos activos");
                    }
                    domiciliario.setDisponible(false);
                    domiciliarioRepository.save(domiciliario);
                    pedido.setEstado("enviado");
                    pedido.setDomiciliarioAsignado(domiciliario);
                }
                case "entregado" -> {
                    pedido.setEstado("entregado");
                    pedido.setFechaEntrega(LocalDateTime.now());
                    if (pedido.getDomiciliarioAsignado() != null) {
                        Domiciliario domAsig = pedido.getDomiciliarioAsignado();
                        domAsig.setDisponible(true);
                        domiciliarioRepository.save(domAsig);
                    }
                }
                default -> {
                    return ResponseEntity.badRequest().body("Estado no soportado: " + nuevoEstado);
                }
            }
            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoActualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // 🔹 POST: Crear o actualizar pedido (carrito)
    // =========================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<?> crearPedido(@RequestBody CreatePedidoRequest body) {
        try {
            if (body == null || body.items == null || body.items.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El pedido no tiene items"));
            }
            if (body.clienteId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Falta clienteId"));
            }

            User user = userRepository.findById(body.clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + body.clienteId));

            // ✅ Si el usuario tiene carrito EN_PROCESO, reutilízalo
            Optional<Pedido> opt = pedidoRepository.findByUserAndEstado(user, "EN_PROCESO");
            Pedido pedido = opt.orElseGet(() -> {
                Pedido p = new Pedido();
                p.setUser(user);
                p.setEstado("EN_PROCESO");
                p.setCreadoEn(LocalDateTime.now());
                return pedidoRepository.saveAndFlush(p);
            });

            // ✅ Limpia ítems anteriores si el pedido está en proceso
            if (pedido.getItems() != null && !pedido.getItems().isEmpty()) {
                for (PedidoComida pc : new ArrayList<>(pedido.getItems())) {
                    if (pc.getAdicionales() != null) {
                        pc.getAdicionales().clear();
                        pedidoComidaRepository.save(pc);
                    }
                }
                pedido.getItems().clear();
                pedidoRepository.saveAndFlush(pedido);
            }

            // ✅ Crea los nuevos ítems
            double total = 0.0;
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

                List<Adicional> ads = (it.adicionalIds == null || it.adicionalIds.isEmpty())
                        ? Collections.emptyList()
                        : adicionalRepository.findAllById(it.adicionalIds);
                pc.setAdicionales(ads);

                double precioAds = ads.stream()
                        .mapToDouble(a -> a.getPrecio() != null ? a.getPrecio() : 0.0)
                        .sum();

                total += (comida.getPrecio() + precioAds) * it.cantidad;

                pedido.getItems().add(pedidoComidaRepository.save(pc));
            }

            pedido.setTotal(total);
            pedido = pedidoRepository.saveAndFlush(pedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(mapPedidoToResponse(pedido));

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo crear el pedido", "detalle", ex.getMessage()));
        }
    }

    // =========================
    // 🔹 PUT: Confirmar pedido (frontend)
    // =========================
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {
        Optional<Pedido> opt = pedidoRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Pedido no encontrado"));
        }

        Pedido pedido = opt.get();
        if (!"EN_PROCESO".equalsIgnoreCase(pedido.getEstado())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El pedido no está en estado EN_PROCESO"));
        }

        pedido.setEstado("CONFIRMADO");
        pedido.setFechaEntrega(null);
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(Map.of("mensaje", "Pedido confirmado correctamente"));
    }

    // =========================
    // 🔹 GET: Pedido por ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> getPedido(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(p -> ResponseEntity.ok(mapPedidoToResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // 🔹 GET: Carrito activo por usuario
    // =========================
    @GetMapping("/carrito/{userId}")
    public ResponseEntity<?> getCarritoUsuario(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }

        Optional<Pedido> pedido = pedidoRepository.findByUserAndEstado(u.get(), "EN_PROCESO");
        return pedido.<ResponseEntity<?>>map(value -> ResponseEntity.ok(mapPedidoToResponse(value)))
                .orElseGet(() -> ResponseEntity.ok(Map.of("mensaje", "El usuario no tiene carrito activo")));
    }

    // =========================
    // 🔹 GET: Pedidos por usuario
    // =========================
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<PedidoResponse>> getPedidosPorUsuario(@PathVariable Long userId) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isEmpty()) return ResponseEntity.badRequest().build();

        User user = u.get();
        List<Pedido> pedidos = pedidoRepository.findByUserOrderByFechaCreacionDesc(user);

        List<PedidoResponse> dtos = pedidos.stream()
                .map(this::mapPedidoToResponse)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    // =========================
    // 🔹 GET: Todos (admin)
    // =========================
    @GetMapping("/todos")
    public List<Pedido> getTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    // =========================
    // 🔹 Mapper entidad -> DTO
    // =========================
    private PedidoResponse mapPedidoToResponse(Pedido p) {
        PedidoResponse r = new PedidoResponse();
        r.id       = p.getId();
        r.estado   = p.getEstado();
        r.total    = p.getTotal();
        r.creadoEn = p.getCreadoEn();

        List<PedidoResponse.Item> list = new ArrayList<>();
        if (p.getItems() != null) {
            for (PedidoComida it : p.getItems()) {
                PedidoResponse.Item ir = new PedidoResponse.Item();
                ir.pedidoComidaId = it.getId();
                ir.comidaId       = it.getComida() != null ? it.getComida().getId() : null;
                ir.comidaNombre   = it.getComida() != null ? it.getComida().getNombre() : null;
                ir.cantidad       = it.getCantidad();

                List<PedidoResponse.AdicionalRes> adrs = new ArrayList<>();
                double precioAdicionales = 0.0;
                if (it.getAdicionales() != null) {
                    for (Adicional ad : it.getAdicionales()) {
                        PedidoResponse.AdicionalRes ar = new PedidoResponse.AdicionalRes();
                        ar.id     = ad.getId();
                        ar.nombre = ad.getNombre();
                        ar.precio = ad.getPrecio();
                        adrs.add(ar);
                        precioAdicionales += ad.getPrecio() != null ? ad.getPrecio() : 0.0;
                    }
                }
                ir.adicionales = adrs;

                double precioComida = it.getComida() != null ? it.getComida().getPrecio() : 0.0;
                ir.subtotal = (precioComida + precioAdicionales) * Math.max(1, it.getCantidad());
                list.add(ir);
            }
        }
        r.items = list;
        return r;
    }
}
