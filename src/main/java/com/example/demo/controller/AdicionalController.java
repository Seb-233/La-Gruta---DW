package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Adicional;
import com.example.demo.model.Comida;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComidaAdicionalRepository;
import com.example.demo.repository.ComidaRepository;

@Controller
@RequestMapping("/la_gruta/adicionales")
public class AdicionalController {

    private final AdicionalRepository adicionalRepo;
    private final CategoriaRepository categoriaRepo;
    private final ComidaRepository comidaRepo;
    private final ComidaAdicionalRepository comidaAdicionalRepo;

    public AdicionalController(AdicionalRepository adicionalRepo, CategoriaRepository categoriaRepo, ComidaRepository comidaRepo, ComidaAdicionalRepository comidaAdicionalRepo) {
        this.adicionalRepo = adicionalRepo;
        this.categoriaRepo = categoriaRepo;
        this.comidaRepo = comidaRepo;
        this.comidaAdicionalRepo = comidaAdicionalRepo;
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
    
        // Primero borrar las relaciones en la tabla intermedia
        comidaAdicionalRepo.deleteByAdicionalId(id);
    
        // Luego borrar el adicional
        adicionalRepo.deleteById(id);
    
        return "redirect:/la_gruta/adicionales";
    }
    
       

    @GetMapping("/todos")
    @ResponseBody
    public List<Map<String, Object>> listarTodosAdicionales() {
        return adicionalRepo.findAll().stream()
                .map(a -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", a.getId());
                    map.put("nombre", a.getNombre());
                    map.put("descripcion", a.getDescripcion());
                    map.put("precio", a.getPrecio());
                    map.put("imagen", a.getImagen());
                    return map;
                })
                .toList();
    }

    @PostMapping("/asociar")
@ResponseBody
public Map<String, String> asociarAdicional(@RequestBody Map<String, Long> payload) {
    Long idComida = payload.get("idComida");
    Long idAdicional = payload.get("idAdicional");

    Map<String, String> response = new HashMap<>();

    try {
        // Obtener la comida y el adicional
        Comida comida = comidaRepo.findById(idComida)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada"));
        Adicional adicional = adicionalRepo.findById(idAdicional)
                .orElseThrow(() -> new RuntimeException("Adicional no encontrado"));

        // Asociar adicional a la comida
        comida.getAdicionales().add(adicional); // suponiendo que Comida tiene Set<Adicional> adicionales
        comidaRepo.save(comida);

        response.put("status", "ok");
        response.put("message", "Adicional agregado correctamente");
    } catch (Exception e) {
        response.put("status", "error");
        response.put("message", e.getMessage());
    }

    return response;
}


}
