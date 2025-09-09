package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;

@Controller
@RequestMapping("/la_gruta/adicionales")
public class AdicionalController {

    private final AdicionalRepository adicionalRepo;
    private final CategoriaRepository categoriaRepo;

    public AdicionalController(AdicionalRepository adicionalRepo, CategoriaRepository categoriaRepo) {
        this.adicionalRepo = adicionalRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @GetMapping
    public String listarAdicionales(Model model) {
        model.addAttribute("adicionales", adicionalRepo.findAll());
        return "tabla_adicionales";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("adicional", new Adicional());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "form-adicional";
    }

    @PostMapping("/guardar")
    public String guardarAdicional(@ModelAttribute Adicional adicional) {
        adicionalRepo.save(adicional);
        return "redirect:/la_gruta/adicionales";
    }

    @GetMapping("/editar/{id}")
    public String editarAdicional(@PathVariable Long id, Model model) {
        return adicionalRepo.findById(id)
                .map(adicional -> {
                    model.addAttribute("adicional", adicional);
                    model.addAttribute("categorias", categoriaRepo.findAll());
                    return "form-adicional";
                })
                .orElse("redirect:/la_gruta/adicionales");
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAdicional(@PathVariable Long id) {
        if (adicionalRepo.existsById(id)) {
            adicionalRepo.deleteById(id);
        }
        return "redirect:/la_gruta/adicionales";
    }
}
