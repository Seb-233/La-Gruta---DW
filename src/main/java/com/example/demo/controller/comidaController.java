package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Comida;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.CategoriaRepository;

@Controller
@RequestMapping("/comidas")
public class ComidaController {

    private final ComidaRepository comidaRepo;
    private final CategoriaRepository categoriaRepo;

    // Constructor Injection (recomendado)
    public ComidaController(ComidaRepository comidaRepo, CategoriaRepository categoriaRepo) {
        this.comidaRepo = comidaRepo;
        this.categoriaRepo = categoriaRepo;
    }

    // Listar todas las comidas en comidaadmin.html
    @GetMapping
    public String listarComidas(Model model) {
        model.addAttribute("comidas", comidaRepo.findAll());
        return "tabla_comidasadmin"; // templates/tabla_comidasadmin.html
    }

    // Mostrar formulario para nueva comida
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("comida", new Comida());
        model.addAttribute("categorias", categoriaRepo.findAll());
        return "form-comida"; // templates/form-comida.html
    }

    // Guardar comida nueva o editada
    @PostMapping("/guardar")
    public String guardarComida(@ModelAttribute Comida comida) {
        comidaRepo.save(comida);
        return "redirect:/comidas";
    }

    // Editar comida (cargar formulario con datos existentes)
    @GetMapping("/editar/{id}")
    public String editarComida(@PathVariable Long id, Model model) {
        return comidaRepo.findById(id)
                .map(comidaExistente -> {
                    model.addAttribute("comida", comidaExistente);
                    model.addAttribute("categorias", categoriaRepo.findAll());
                    return "form-comida";
                })
                .orElse("redirect:/comidas"); // si no existe redirige
    }

    // Eliminar comida
    @GetMapping("/eliminar/{id}")
    public String eliminarComida(@PathVariable Long id) {
        if (comidaRepo.existsById(id)) {
            comidaRepo.deleteById(id);
        }
        return "redirect:/comidas";
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // nombre del template index.html en templates/
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu"; // nombre del template menu.html en templates/
    }

    @GetMapping("/restaurantes")
    public String restaurantes() {
        return "restaurantes"; // nombre del template restaurantes.html en templates/
    }
}
