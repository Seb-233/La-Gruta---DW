package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;

@Controller
@RequestMapping("/adicionales")
public class AdicionalController {

    private final AdicionalRepository adicionalRepo;
    private final CategoriaRepository categoriaRepo;

    public AdicionalController(AdicionalRepository adicionalRepo, CategoriaRepository categoriaRepo) {
        this.adicionalRepo = adicionalRepo;
        this.categoriaRepo = categoriaRepo;
    }

    // Listar todos los adicionales
    @GetMapping
    public String listarAdicionales(Model model) {
        model.addAttribute("adicionales", adicionalRepo.findAll());
        return "tabla_adicionales"; // templates/tabla_adicionales.html
    }

    // Mostrar formulario para nuevo adicional
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("adicional", new Adicional());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "form-adicional"; // templates/form-adicional.html
    }

    // Guardar adicional
    @PostMapping("/guardar")
    public String guardarAdicional(@ModelAttribute Adicional adicional) {
        adicionalRepo.save(adicional);
        return "redirect:/adicionales";
    }

    // Editar adicional
    @GetMapping("/editar/{id}")
    public String editarAdicional(@PathVariable Long id, Model model) {
        return adicionalRepo.findById(id)
                .map(adicional -> {
                    model.addAttribute("adicional", adicional);
                    model.addAttribute("categorias", categoriaRepo.findAll());
                    return "form-adicional";
                })
                .orElse("redirect:/adicionales");
    }

    // Eliminar adicional
    @GetMapping("/eliminar/{id}")
    public String eliminarAdicional(@PathVariable Long id) {
        if (adicionalRepo.existsById(id)) {
            adicionalRepo.deleteById(id);
        }
        return "redirect:/adicionales";
    }
}

