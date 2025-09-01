package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.model.comida;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.adicionalRepository;
import com.example.demo.repository.comidaRepository;

@Controller
@RequestMapping("/orden")
public class OrdenController {

    @Autowired
    private comidaRepository comidaRepository;

    @Autowired
    private adicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Página principal de iniciar orden
     */
    @GetMapping("/iniciar")
    public String iniciarOrden(Model model) {
        // Cargar todas las categorías para el menú
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        
        return "iniciar-orden"; // templates/iniciar-orden.html
    }

    /**
     * API para obtener el menú completo organizado por categorías
     */
    @GetMapping("/api/menu")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMenuCompleto() {
        try {
            Map<String, Object> response = new HashMap<>();
            List<Categoria> categorias = categoriaRepository.findAll();
            
            Map<String, List<Map<String, Object>>> menuPorCategoria = new HashMap<>();
            
            for (Categoria categoria : categorias) {
                List<comida> comidas = comidaRepository.findByCategoriaId(categoria.getId());
                List<Map<String, Object>> comidasData = new ArrayList<>();
                
                for (comida c : comidas) {
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
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al cargar el menú");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * API para obtener comidas por categoría
     */
    @GetMapping("/api/menu/{categoriaSlug}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getComidasPorCategoria(@PathVariable String categoriaSlug) {
        try {
            Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(categoriaSlug);
            if (categoriaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Categoria categoria = categoriaOpt.get();
            List<comida> comidas = comidaRepository.findByCategoriaId(categoria.getId());
            List<Map<String, Object>> response = new ArrayList<>();
            
            for (comida c : comidas) {
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API para obtener adicionales por categoría
     */
    @GetMapping("/api/adicionales/categoria/{categoriaSlug}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAdicionalesPorCategoria(@PathVariable String categoriaSlug) {
        try {
            Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(categoriaSlug);
            if (categoriaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Categoria categoria = categoriaOpt.get();
            List<Adicional> adicionales = adicionalRepository.findByCategoriaId(categoria.getId());
            List<Map<String, Object>> response = new ArrayList<>();
            
            for (Adicional a : adicionales) {
                Map<String, Object> adicionalData = new HashMap<>();
                adicionalData.put("id", a.getId());
                adicionalData.put("name", a.getNombre());
                adicionalData.put("price", a.getPrecio());
                adicionalData.put("image", a.getImagen());
                adicionalData.put("description", a.getDescripcion());
                response.add(adicionalData);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API para obtener adicionales por comida específica
     */
    @GetMapping("/api/adicionales/comida/{comidaId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAdicionalesPorComida(@PathVariable Long comidaId) {
        try {
            Optional<comida> comidaOpt = comidaRepository.findById(comidaId);
            if (comidaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            comida c = comidaOpt.get();
            if (c.getCategoria() == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            List<Adicional> adicionales = adicionalRepository.findByCategoriaId(c.getCategoria().getId());
            List<Map<String, Object>> response = new ArrayList<>();
            
            for (Adicional a : adicionales) {
                Map<String, Object> adicionalData = new HashMap<>();
                adicionalData.put("id", a.getId());
                adicionalData.put("name", a.getNombre());
                adicionalData.put("price", a.getPrecio());
                adicionalData.put("image", a.getImagen());
                adicionalData.put("description", a.getDescripcion());
                response.add(adicionalData);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API para obtener información de una comida específica
     */
    @GetMapping("/api/comida/{comidaId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getComidaDetalle(@PathVariable Long comidaId) {
        try {
            Optional<comida> comidaOpt = comidaRepository.findById(comidaId);
            if (comidaOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            comida c = comidaOpt.get();
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
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * API para obtener todas las categorías
     */
    @GetMapping("/api/categorias")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCategorias() {
        try {
            List<Categoria> categorias = categoriaRepository.findAll();
            List<Map<String, Object>> response = new ArrayList<>();
            
            for (Categoria categoria : categorias) {
                Map<String, Object> categoriaData = new HashMap<>();
                categoriaData.put("id", categoria.getId());
                categoriaData.put("name", categoria.getNombre());
                categoriaData.put("slug", categoria.getSlug());
                categoriaData.put("description", categoria.getDescripcion());
                categoriaData.put("image", categoria.getImagen());
                response.add(categoriaData);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

