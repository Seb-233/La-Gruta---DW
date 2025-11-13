package com.example.demo.controller;

import com.example.demo.model.Domiciliario;
import com.example.demo.service.DomiciliarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/domiciliarios")
@CrossOrigin(origins = "*") // para permitir peticiones desde Angular
public class DomiciliarioController {

    @Autowired
    private DomiciliarioService domiciliarioService;

    @GetMapping
    public List<Domiciliario> getAll() {
        return domiciliarioService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domiciliario> getById(@PathVariable Long id) {
        Optional<Domiciliario> domiciliario = domiciliarioService.getById(id);
        return domiciliario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Domiciliario create(@RequestBody Domiciliario domiciliario) {
        return domiciliarioService.save(domiciliario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domiciliario> update(@PathVariable Long id, @RequestBody Domiciliario updated) {
        return domiciliarioService.getById(id).map(d -> {
            updated.setId(id);
            return ResponseEntity.ok(domiciliarioService.save(updated));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domiciliarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
