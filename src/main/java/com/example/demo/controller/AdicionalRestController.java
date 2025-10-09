package com.example.demo.controller;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalRestController {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Adicional> listarAdicionales() {
        return adicionalRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adicional> obtenerAdicionalPorId(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Adicional> crearAdicional(@RequestBody Adicional adicional) {
        if (adicional.getCategorias() != null && !adicional.getCategorias().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>();
            for (Categoria cat : adicional.getCategorias()) {
                categoriaRepository.findById(cat.getId()).ifPresent(categorias::add);
            }
            adicional.setCategorias(categorias);
        }
        Adicional guardado = adicionalRepository.save(adicional);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adicional> actualizarAdicional(
            @PathVariable Long id,
            @RequestBody Adicional detallesAdicional) {

        return adicionalRepository.findById(id)
                .map(adicionalExistente -> {
                    adicionalExistente.setNombre(detallesAdicional.getNombre());
                    adicionalExistente.setDescripcion(detallesAdicional.getDescripcion());
                    adicionalExistente.setPrecio(detallesAdicional.getPrecio());
                    adicionalExistente.setDisponible(detallesAdicional.getDisponible());
                    adicionalExistente.setImagen(detallesAdicional.getImagen());

                    if (detallesAdicional.getCategorias() != null) {
                        Set<Categoria> nuevasCategorias = new HashSet<>();
                        for (Categoria cat : detallesAdicional.getCategorias()) {
                            categoriaRepository.findById(cat.getId()).ifPresent(nuevasCategorias::add);
                        }
                        adicionalExistente.setCategorias(nuevasCategorias);
                    }

                    Adicional actualizado = adicionalRepository.save(adicionalExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdicional(@PathVariable Long id) {
        if (!adicionalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        adicionalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{slug}")
    public ResponseEntity<List<Adicional>> getByCategoriaSlug(@PathVariable String slug) {
        List<Adicional> adicionales = adicionalRepository.findByCategorias_Slug(slug);
        return ResponseEntity.ok(adicionales);
    }
}
