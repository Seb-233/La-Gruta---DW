package com.example.demo.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.model.Comida;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.ComidaRepository;

@Controller
@RequestMapping("/la_gruta/orden")
public class OrdenController {

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/iniciar")
    public String iniciarOrden(Model model) {
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        return "iniciar-orden";
    }

    @GetMapping("/api/menu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMenuCompleto() {
        try {
            Map<String, Object> response = new HashMap<>();
            List<Categoria> categorias = categoriaRepository.findAll();
            Map<String, List<Map<String, Object>>> menuPorCategoria = new HashMap<>();
            for (Categoria categoria : categorias) {
                List<Comida> comidas = comidaRepository.findByCategoriaId(categoria.getId());
                List<Map<String, Object>> comidasData = new ArrayList<>();
                for (Comida c : comidas) {
                    Map<String, Object> comidaData = new HashMap<>();
                    comidaData.put("id", c.getId());
                    comidaData.put("name", c.getNombre());
                    comidaData.put("description", c.getDescripcion());
                    comidaData.put("price", c.getPrecio());
                    comidaData.put("image", c.getImagen());
                    comidaData.put("category", categoria.getSlug());
                    comidasData.add(comidaData);
                }
                menuPorCategoria.put(categoria.getSlug(), comidasData);
            }
            response.put("menu", menuPorCategoria);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/menu/{categoriaSlug}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getComidasPorCategoria(@PathVariable String categoriaSlug) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(categoriaSlug);
        if (categoriaOpt.isEmpty()) return ResponseEntity.notFound().build();
        Categoria categoria = categoriaOpt.get();
        List<Comida> comidas = comidaRepository.findByCategoriaId(categoria.getId());
        List<Map<String, Object>> response = new ArrayList<>();
        for (Comida c : comidas) {
            Map<String, Object> comidaData = new HashMap<>();
            comidaData.put("id", c.getId());
            comidaData.put("name", c.getNombre());
            comidaData.put("description", c.getDescripcion());
            comidaData.put("price", c.getPrecio());
            comidaData.put("image", c.getImagen());
            comidaData.put("category", categoria.getSlug());
            response.add(comidaData);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/adicionales/categoria/{categoriaSlug}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAdicionalesPorCategoria(@PathVariable String categoriaSlug) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(categoriaSlug);
        if (categoriaOpt.isEmpty()) return ResponseEntity.notFound().build();
        List<Adicional> adicionales = adicionalRepository.findByCategoriaId(categoriaOpt.get().getId());
        List<Map<String, Object>> response = new ArrayList<>();
        for (Adicional a : adicionales) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", a.getId());
            data.put("name", a.getNombre());
            data.put("price", a.getPrecio());
            data.put("image", a.getImagen());
            data.put("description", a.getDescripcion());
            response.add(data);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/adicionales/comida/{comidaId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAdicionalesPorComida(@PathVariable Long comidaId) {
        Optional<Comida> comidaOpt = comidaRepository.findById(comidaId);
        if (comidaOpt.isEmpty()) return ResponseEntity.notFound().build();
        Comida c = comidaOpt.get();
        if (c.getCategoria() == null) return ResponseEntity.ok(new ArrayList<>());
        List<Adicional> adicionales = adicionalRepository.findByCategoriaId(c.getCategoria().getId());
        List<Map<String, Object>> response = new ArrayList<>();
        for (Adicional a : adicionales) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", a.getId());
            data.put("name", a.getNombre());
            data.put("price", a.getPrecio());
            data.put("image", a.getImagen());
            data.put("description", a.getDescripcion());
            response.add(data);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/comida/{comidaId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getComidaDetalle(@PathVariable Long comidaId) {
        Optional<Comida> comidaOpt = comidaRepository.findById(comidaId);
        if (comidaOpt.isEmpty()) return ResponseEntity.notFound().build();
        Comida c = comidaOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", c.getId());
        response.put("name", c.getNombre());
        response.put("description", c.getDescripcion());
        response.put("price", c.getPrecio());
        response.put("image", c.getImagen());
        if (c.getCategoria() != null) {
            response.put("category", c.getCategoria().getSlug());
            response.put("categoryName", c.getCategoria().getNombre());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/categorias")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Categoria categoria : categorias) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", categoria.getId());
            data.put("name", categoria.getNombre());
            data.put("slug", categoria.getSlug());
            data.put("description", categoria.getDescripcion());
            data.put("image", categoria.getImagen());
            response.add(data);
        }
        return ResponseEntity.ok(response);
    }
}
