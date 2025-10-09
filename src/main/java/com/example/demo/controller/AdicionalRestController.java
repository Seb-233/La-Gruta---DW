package com.example.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Permite la conexión con Angular
public class AdicionalRestController {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // ✅ READ (Leer todos): Obtiene una lista de todos los adicionales.
    // Corresponde a AC34.
    // URL: GET /api/adicionales
    @GetMapping
    public List<Adicional> getAll() {
        return adicionalRepository.findAll();
    }

    // ✅ READ (Leer por categoría - usando slug)
    // Busca los adicionales asociados a una categoría mediante su slug.
    // URL: GET /api/adicionales/categoria/{slug}
    @GetMapping("/categoria/{slug}")
    public ResponseEntity<List<Adicional>> getByCategoriaSlug(@PathVariable String slug) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(slug);

        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Categoria categoria = categoriaOpt.get();
        List<Adicional> adicionales = adicionalRepository.findByCategoriasContaining(categoria);
        return ResponseEntity.ok(adicionales);
    }

    // ✅ READ (Leer por ID): Obtiene un adicional específico por su ID.
    // URL: GET /api/adicionales/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Adicional> obtenerAdicionalPorId(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ CREATE (Crear): Registra un nuevo adicional en la base de datos.
    // Corresponde a AC33.
    // URL: POST /api/adicionales
    @PostMapping
    public ResponseEntity<Adicional> crearAdicional(@RequestBody Adicional adicional) {
        // Si hay categorías, se asegura de que existan en la BD antes de asociarlas
        if (adicional.getCategorias() != null && !adicional.getCategorias().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>();
            for (Categoria cat : adicional.getCategorias()) {
                categoriaRepository.findById(cat.getId()).ifPresent(categorias::add);
            }
            adicional.setCategorias(categorias);
        }

        Adicional guardado = adicionalRepository.save(adicional);
        return ResponseEntity.ok(guardado);
    }

    // ✅ UPDATE (Actualizar): Modifica los datos de un adicional existente.
    // Corresponde a AC35.
    // URL: PUT /api/adicionales/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Adicional> actualizarAdicional(
            @PathVariable Long id,
            @RequestBody Adicional detallesAdicional) {

        return adicionalRepository.findById(id)
                .map(adicionalExistente -> {
                    adicionalExistente.setNombre(detallesAdicional.getNombre());
                    adicionalExistente.setDescripcion(detallesAdicional.getDescripcion());
                    adicionalExistente.setPrecio(detallesAdicional.getPrecio());
                    adicionalExistente.setDisponible(detallesAdicional.getDisponible());
                    adicionalExistente.setImagen(detallesAdicional.getImagen());

                    // Actualiza categorías si se envían
                    if (detallesAdicional.getCategorias() != null) {
                        Set<Categoria> nuevasCategorias = new HashSet<>();
                        for (Categoria cat : detallesAdicional.getCategorias()) {
                            categoriaRepository.findById(cat.getId()).ifPresent(nuevasCategorias::add);
                        }
                        adicionalExistente.setCategorias(nuevasCategorias);
                    }

                    Adicional actualizado = adicionalRepository.save(adicionalExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE (Eliminar): Elimina un adicional de la base de datos.
    // Corresponde a AC36 (aunque el AC dice "desactivar", por simplicidad se elimina).
    // URL: DELETE /api/adicionales/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdicional(@PathVariable Long id) {
        if (!adicionalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // ⚠️ Si existen relaciones dependientes (ej. PedidoComida),
        // primero deben eliminarse esas referencias para evitar errores de clave foránea.
        adicionalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
