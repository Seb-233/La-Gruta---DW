package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.comidaService;

@Controller
@RequestMapping("/la_gruta")
//http://localhost:8080/la_gruta/index
public class indexController {

    @Autowired
    private comidaService comidaService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("comidas", comidaService.SearchAll());
        return "menu";
    }

    @GetMapping("/plato_estrella")
    public String platoEstrella() {
        return "plato-estrella";
    }

    @GetMapping("/restaurantes")
    public String restaurantes() {
        return "restaurantes";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/tabla_comidas")
    public String tablaComidas(Model model) {
        model.addAttribute("comidas", comidaService.SearchAll());
        return "tabla-comidas";
    }

    // Nueva ruta para iniciar orden
    @GetMapping("/iniciar-orden")
    public String iniciarOrden() {
        return "iniciar-orden"; // templates/iniciar-orden.html
    }
}