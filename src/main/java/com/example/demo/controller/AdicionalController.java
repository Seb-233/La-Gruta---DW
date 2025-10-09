package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalCategoriaRepository;
import com.example.demo.repository.AdicionalRepository;

@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalController {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private AdicionalCategoriaRepository adicionalCategoriaRepository;

    // ✅ Listar todos los adicionales
    @GetMapping
    public List<Adicional> getAll() {
        return adicionalRepository.findAll();
    }

    // ✅ Obtener adicional por ID
    @GetMapping("/{id}")
    public ResponseEntity<Adicional> getById(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Crear un nuevo adicional
    @PostMapping
    public Adicional create(@RequestBody Adicional adicional) {
        return adicionalRepository.save(adicional);
    }

    // ✅ Actualizar un adicional existente
    @PutMapping("/{id}")
    public ResponseEntity<Adicional> update(@PathVariable Long id, @RequestBody Adicional adicionalDetails) {
        return adicionalRepository.findById(id)
                .map(adicional -> {
                    adicional.setNombre(adicionalDetails.getNombre());
                    adicional.setDescripcion(adicionalDetails.getDescripcion());
                    adicional.setPrecio(adicionalDetails.getPrecio());
                    adicional.setCategorias(adicionalDetails.getCategorias());
                    Adicional updated = adicionalRepository.save(adicional);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar adicional por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(adicional -> {
                    adicionalRepository.delete(adicional);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
