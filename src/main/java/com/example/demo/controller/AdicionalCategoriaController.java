package com.example.demo.controller;

import com.example.demo.model.AdicionalCategoria;
import com.example.demo.repository.AdicionalCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adicional-categoria")
@CrossOrigin(origins = "http://localhost:4200")
public class AdicionalCategoriaController {

    @Autowired
    private AdicionalCategoriaRepository adicionalCategoriaRepository;

    @GetMapping
    public List<AdicionalCategoria> listarTodo() {
        return adicionalCategoriaRepository.findAll();
    }

    @GetMapping("/adicional/{id}")
    public List<AdicionalCategoria> listarPorAdicional(@PathVariable Long id) {
        return adicionalCategoriaRepository.findByAdicionalId(id);
    }

    @PostMapping
    public AdicionalCategoria crear(@RequestBody AdicionalCategoria adicionalCategoria) {
        return adicionalCategoriaRepository.save(adicionalCategoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        adicionalCategoriaRepository.deleteById(id);
    }
}
