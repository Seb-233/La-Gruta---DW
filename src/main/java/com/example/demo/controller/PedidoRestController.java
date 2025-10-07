package com.example.demo.controller;

import com.example.demo.model.Domiciliario;
import com.example.demo.model.Pedido;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoRestController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    // ENDPOINT PARA LEER TODOS LOS PEDIDOS ACTIVOS (AC22)
    @GetMapping
    public List<Pedido> getPedidosActivos() {
        return pedidoRepository.findAllActive();
    }

    // ENDPOINT PARA ACTUALIZAR EL ESTADO DE UN PEDIDO (AC23, AC24, AC25)
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) { // <--
                                                                                                              // CAMBIO
                                                                                                              // 1: De
                                                                                                              // <Pedido>
                                                                                                              // a <?>
        String nuevoEstado = body.get("estado");

        return pedidoRepository.findById(id).map(pedido -> {

            switch (nuevoEstado) {
                case "cocinando": // AC23
                    pedido.setEstado("cocinando");
                    break;

                case "enviado": // AC24
                    Domiciliario domiciliario = domiciliarioRepository.findAll().stream()
                            .filter(d -> d.isDisponible())
                            .findFirst()
                            .orElse(null);

                    if (domiciliario != null) {
                        domiciliario.setDisponible(false);
                        domiciliarioRepository.save(domiciliario);

                        pedido.setEstado("enviado");
                        pedido.setDomiciliarioAsignado(domiciliario);
                    } else {
                        // CAMBIO 2: Ahora devolvemos una respuesta con un mensaje de error
                        return ResponseEntity.badRequest().body("No hay domiciliarios disponibles");
                    }
                    break;

                case "entregado": // AC25
                    pedido.setEstado("entregado");
                    pedido.setFechaEntrega(LocalDateTime.now());

                    if (pedido.getDomiciliarioAsignado() != null) {
                        Domiciliario domiciliarioAsignado = pedido.getDomiciliarioAsignado();
                        domiciliarioAsignado.setDisponible(true);
                        domiciliarioRepository.save(domiciliarioAsignado);
                    }
                    break;
            }

            Pedido pedidoActualizado = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoActualizado);

        }).orElse(ResponseEntity.notFound().build());
    }
}