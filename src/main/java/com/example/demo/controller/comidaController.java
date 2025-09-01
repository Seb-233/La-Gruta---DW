package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.comida;
import com.example.demo.repository.comidaRepository;

@Controller
@RequestMapping("/comidas")
public class comidaController {

    @Autowired
    private comidaRepository comidaRepo;

    // Listar todas las comidas en comidaadmin.html
    @GetMapping
    public String listarComidas(Model model) {
        model.addAttribute("comidas", comidaRepo.findAll());
        return "tabla_comidasadmin"; // templates/tabla_comidasadmin.html
    }

    // Mostrar formulario para nueva comida
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("comida", new comida());
        return "form-comida"; // templates/form-comida.html
    }

    // Guardar comida nueva o editada
    @PostMapping("/guardar")
    public String guardarComida(@ModelAttribute comida comida) {
        comidaRepo.save(comida);
        return "redirect:/comidas";
    }

    // Editar comida (cargar formulario con datos existentes)
    @GetMapping("/editar/{id}")
    public String editarComida(@PathVariable Long id, Model model) {
        comida comidaExistente = comidaRepo.findById(id).orElse(null);
        model.addAttribute("comida", comidaExistente);
        return "form-comida";
    }

    // Eliminar comida
    @GetMapping("/eliminar/{id}")
    public String eliminarComida(@PathVariable Long id) {
        comidaRepo.deleteById(id);
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
