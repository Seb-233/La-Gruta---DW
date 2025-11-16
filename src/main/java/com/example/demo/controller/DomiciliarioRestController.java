package com.example.demo.controller;

import com.example.demo.model.Domiciliario;
import com.example.demo.repository.DomiciliarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/domiciliarios", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class DomiciliarioRestController {

    private final DomiciliarioRepository domiciliarioRepository;

    // =============================
    // 🔹 GET: Todos los domiciliarios
    // =============================
    @GetMapping
    public List<Domiciliario> getAll() {
        return domiciliarioRepository.findAll();
    }

    // =============================
    // 🔹 GET: Disponibles
    // =============================
    @GetMapping("/disponibles")
    public List<Domiciliario> getDisponibles() {
        return domiciliarioRepository.findByDisponibleTrue();
    }

    // =============================
    // 🔹 POST: Crear domiciliario
    // =============================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Domiciliario d) {

        if (domiciliarioRepository.existsByCedula(d.getCedula())) {
            return ResponseEntity.badRequest().body(Map.of("error", "La cédula ya existe"));
        }

        domiciliarioRepository.save(d);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Domiciliario creado correctamente"));
    }

    // =============================
    // 🔹 PUT: Actualizar domiciliario
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Domiciliario data) {

        return domiciliarioRepository.findById(id)
                .map(dom -> {
                    dom.setNombre(data.getNombre());
                    dom.setCelular(data.getCelular());
                    dom.setDisponible(data.isDisponible());
                    domiciliarioRepository.save(dom);

                    return ResponseEntity.ok(Map.of("mensaje", "Domiciliario actualizado"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Domiciliario no encontrado")));
    }

    // =============================
    // 🔹 DELETE: Eliminar
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!domiciliarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Domiciliario no encontrado"));
        }

        domiciliarioRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("mensaje", "Domiciliario eliminado"));
    }
}
