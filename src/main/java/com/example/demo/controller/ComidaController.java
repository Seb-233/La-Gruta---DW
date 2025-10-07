package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Comida;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.CategoriaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/la_gruta/comidas")
public class ComidaController {

    private final ComidaRepository comidaRepo;
    private final CategoriaRepository categoriaRepo;

    public ComidaController(ComidaRepository comidaRepo, CategoriaRepository categoriaRepo) {
        this.comidaRepo = comidaRepo;
        this.categoriaRepo = categoriaRepo;
    }

    // ----- ADMIN -----
    @GetMapping("/admin")
    public String listarComidasAdmin(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/la_gruta/login"; // Seguridad básica
        }
        model.addAttribute("comidas", comidaRepo.findAll());
        return "tabla_comidasadmin";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/la_gruta/index";
        }
        model.addAttribute("comida", new Comida());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "form-comida";
    }

    @PostMapping("/guardar")
    public String guardarComida(@ModelAttribute Comida comida, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/la_gruta/index";
        }
        comidaRepo.save(comida);
        return "redirect:/la_gruta/comidas/admin";
    }

    @GetMapping("/editar/{id}")
    public String editarComida(@PathVariable Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/la_gruta/index";
        }

        return comidaRepo.findById(id)
                .map(comidaExistente -> {
                    model.addAttribute("comida", comidaExistente);
                    model.addAttribute("categorias", categoriaRepo.findAll());
                    return "form-comida";
                })
                .orElse("redirect:/la_gruta/comidas/admin");
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarComida(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            return "redirect:/la_gruta/index";
        }

        if (comidaRepo.existsById(id)) {
            comidaRepo.deleteById(id);
        }
        return "redirect:/la_gruta/comidas/admin";
    }

    // ----- CLIENT -----
    @GetMapping
    public String listarComidasCliente(Model model) {
        model.addAttribute("comidas", comidaRepo.findAll());
        return "tabla_comidas"; // Vista cliente
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

    @GetMapping("/restaurantes")
    public String restaurantes() {
        return "restaurantes";
    }
}
