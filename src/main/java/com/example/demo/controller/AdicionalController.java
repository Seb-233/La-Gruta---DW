package com.example.demo.controller;

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
//@RestController
//@RequestMapping("/api/adicionales")
public class AdicionalController {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @GetMapping
    public List<Adicional> getAll() {
        return adicionalRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adicional> getById(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Adicional create(@RequestBody Adicional adicional) {
        return adicionalRepository.save(adicional);
    }

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