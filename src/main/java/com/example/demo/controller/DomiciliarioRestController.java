package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Domiciliario;
import com.example.demo.repository.DomiciliarioRepository;

@RestController
@RequestMapping(value = "/api/domiciliarios", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class DomiciliarioRestController {

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    // ============================
    // 🔹 GET: Todos los domiciliarios
    // ============================
    @GetMapping
    public List<Domiciliario> getAll() {
        return domiciliarioRepository.findAll();
    }

    // ============================
    // 🔹 GET: Domiciliario por ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Domiciliario> optional = domiciliarioRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Domiciliario no encontrado"));
        }

        return ResponseEntity.ok(optional.get());
    }

    // ============================
    // 🔹 GET: Domiciliarios disponibles
    // ============================
    @GetMapping("/disponibles")
    public List<Domiciliario> getDisponibles() {
        return domiciliarioRepository.findByDisponibleTrue();
    }

    // ============================
    // 🔹 POST: Crear domiciliario
    // ============================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Domiciliario d) {

        // Validar campos obligatorios
        if (d.getNombre() == null || d.getNombre().isBlank()
                || d.getCedula() == null || d.getCedula().isBlank()
                || d.getCorreo() == null || d.getCorreo().isBlank()
                || d.getTelefono() == null || d.getTelefono().isBlank()
                || d.getVehiculo() == null || d.getVehiculo().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos obligatorios deben estar completos"));
        }

        // Validar cédula única
        if (domiciliarioRepository.existsByCedula(d.getCedula())) {
            return ResponseEntity.badRequest().body(Map.of("error", "La cédula ya existe"));
        }

        domiciliarioRepository.save(d);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Domiciliario creado correctamente"));
    }

    // ============================
    // 🔹 PUT: Actualizar domiciliario
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Domiciliario data) {

        Optional<Domiciliario> optional = domiciliarioRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Domiciliario no encontrado"));
        }

        Domiciliario dom = optional.get();

        // Actualizar campos
        dom.setNombre(data.getNombre());
        dom.setCorreo(data.getCorreo());
        dom.setTelefono(data.getTelefono());
        dom.setVehiculo(data.getVehiculo());
        dom.setPlaca(data.getPlaca());
        dom.setZonaCobertura(data.getZonaCobertura());
        dom.setDisponible(data.isDisponible());

        domiciliarioRepository.save(dom);

        return ResponseEntity.ok(Map.of("mensaje", "Domiciliario actualizado correctamente"));
    }

    // ============================
    // 🔹 DELETE: Eliminar domiciliario
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!domiciliarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Domiciliario no encontrado"));
        }

        domiciliarioRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("mensaje", "Domiciliario eliminado correctamente"));
    }
}
