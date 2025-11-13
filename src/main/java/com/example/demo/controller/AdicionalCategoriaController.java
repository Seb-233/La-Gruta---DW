package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Adicional;
import com.example.demo.model.AdicionalCategoria;
import com.example.demo.model.Categoria;
import com.example.demo.repository.AdicionalCategoriaRepository;

@RestController
@RequestMapping("/api/adicional-categoria")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalCategoriaController {

    @Autowired
    private AdicionalCategoriaRepository adicionalCategoriaRepository;

    // ✅ Listar todas las relaciones
    @GetMapping
    public List<AdicionalCategoria> listarTodas() {
        return adicionalCategoriaRepository.findAll();
    }

    // ✅ Listar relaciones por adicional
    @GetMapping("/adicional/{id}")
    public List<AdicionalCategoria> listarPorAdicional(@PathVariable Long id) {
        Adicional adicional = new Adicional();
        adicional.setId(id);
        return adicionalCategoriaRepository.findByAdicional(adicional);
    }

    // ✅ Listar relaciones por categoría
    @GetMapping("/categoria/{id}")
    public List<AdicionalCategoria> listarPorCategoria(@PathVariable Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        return adicionalCategoriaRepository.findByCategoria(categoria);
    }

    // ✅ Crear nueva relación
    @PostMapping
    public AdicionalCategoria crear(@RequestBody AdicionalCategoria adicionalCategoria) {
        return adicionalCategoriaRepository.save(adicionalCategoria);
    }

    // ✅ Eliminar relación por ID
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        adicionalCategoriaRepository.deleteById(id);
    }
}
