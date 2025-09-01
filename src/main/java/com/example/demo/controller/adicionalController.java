package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.model.comida;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.adicionalRepository;
import com.example.demo.repository.comidaRepository;

@Controller
public class adicionalController {

    @Autowired
    private comidaRepository comidaRepository;

    @Autowired
    private adicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /** 
     * Vista: adicionales para una comida específica.
     * Toma la CATEGORÍA de la comida y lista sus adicionales.
     */
    @GetMapping("/adicionales/comida/{id}")
    public String adicionalesPorComida(@PathVariable Long id, Model model) {
        Optional<comida> maybe = comidaRepository.findById(id);
        if (maybe.isEmpty()) {
            // si la comida no existe, muestro vista general
            model.addAttribute("adicionales", adicionalRepository.findAll());
            return "adicionales";
        }

        comida c = maybe.get();
        List<Adicional> lista = Collections.emptyList();

        if (c.getCategoria() != null) {
            // usa el método por categoriaId (ajústalo si tu repo expone otro nombre)
            lista = adicionalRepository.findByCategoriaId(c.getCategoria().getId());
        }

        model.addAttribute("comida", c);
        model.addAttribute("adicionales", lista);
        return "adicionales";
    }


   @GetMapping("/adicionales/view")
public String verAdicionales(Model model) {
    model.addAttribute("adicionales", adicionalRepository.findAll());
    return "adicionales"; // -> templates/adicionales.html
}



   
    @GetMapping("/adicionales/api/comida/{id}")
    @ResponseBody
    public List<Map<String, Object>> apiAdicionalesPorComida(@PathVariable Long id) {
        comida c = comidaRepository.findById(id).orElse(null);
        if (c == null || c.getCategoria() == null) return List.of();

        List<Adicional> lista = adicionalRepository.findByCategoriaId(c.getCategoria().getId());
        List<Map<String, Object>> out = new ArrayList<>();
        for (Adicional a : lista) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", a.getId());
            m.put("nombre", a.getNombre());
            m.put("precio", a.getPrecio());
            out.add(m);
        }
        return out;
    }

    @GetMapping("/adicionales/categoria/{slug}")
    public String verPorCategoria(@PathVariable String slug, Model model) {
        Categoria cat = categoriaRepository.findBySlug(slug).orElse(null);
        List<Adicional> lista = (cat == null) ? List.of() : adicionalRepository.findByCategoriaId(cat.getId());
        model.addAttribute("adicionales", lista);
        return "adicionales";
    }
}
