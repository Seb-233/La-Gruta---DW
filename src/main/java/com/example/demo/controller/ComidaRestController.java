package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Comida;
import com.example.demo.repository.ComidaRepository;

@RestController
@RequestMapping("/api/comidas")
@CrossOrigin(origins = "http://localhost:4200") // permite llamadas desde Angular
public class ComidaRestController {

    private final ComidaRepository comidaRepo;

    public ComidaRestController(ComidaRepository comidaRepo) {
        this.comidaRepo = comidaRepo;
    }

    // --------- GET ALL ---------
    @GetMapping
    public List<Comida> listar() {
        return comidaRepo.findAll();
    }

    // --------- GET BY ID ---------
    @GetMapping("/{id}")
    public ResponseEntity<Comida> obtenerPorId(@PathVariable Long id) {
        Optional<Comida> comida = comidaRepo.findById(id);
        return comida.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // --------- CREATE ---------
    @PostMapping
    public Comida crear(@RequestBody Comida comida) {
        return comidaRepo.save(comida);
    }

    // --------- UPDATE ---------
    @PutMapping("/{id}")
    public ResponseEntity<Comida> actualizar(@PathVariable Long id, @RequestBody Comida comidaActualizada) {
        return comidaRepo.findById(id)
            .map(comida -> {
                comida.setNombre(comidaActualizada.getNombre());
                comida.setDescripcion(comidaActualizada.getDescripcion());
                comida.setPrecio(comidaActualizada.getPrecio());
                comida.setImagen(comidaActualizada.getImagen());
                comida.setCategoria(comidaActualizada.getCategoria());
                comida.setDisponible(comidaActualizada.getDisponible());
                comida.setTiempoPreparacion(comidaActualizada.getTiempoPreparacion());
                comida.setEsEspecialidad(comidaActualizada.getEsEspecialidad());
                comida.setIngredientes(comidaActualizada.getIngredientes());
                comida.setAdicionales(comidaActualizada.getAdicionales());
                return ResponseEntity.ok(comidaRepo.save(comida));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // --------- DELETE ---------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (comidaRepo.existsById(id)) {
            comidaRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
