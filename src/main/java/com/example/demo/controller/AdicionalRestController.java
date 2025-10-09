package com.example.demo.controller;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200") // Permite la conexión con Angular
public class AdicionalRestController {

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * READ (Leer todos): Obtiene una lista de todos los adicionales.
     * Corresponde a AC34.
     * URL: GET /api/adicionales
     */
    @GetMapping
    public List<Adicional> listarAdicionales() {
        return adicionalRepository.findAll();
    }

    /**
     * READ (Leer por ID): Obtiene un adicional específico por su ID.
     * URL: GET /api/adicionales/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Adicional> obtenerAdicionalPorId(@PathVariable Long id) {
        return adicionalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * CREATE (Crear): Registra un nuevo adicional en la base de datos.
     * Corresponde a AC33.
     * URL: POST /api/adicionales
     */
    @PostMapping
    public ResponseEntity<Adicional> crearAdicional(@RequestBody Adicional adicional) {
        if (adicional.getCategorias() != null && !adicional.getCategorias().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>();
            for (Categoria cat : adicional.getCategorias()) {
                Optional<Categoria> categoriaReal = categoriaRepository.findById(cat.getId());
                categoriaReal.ifPresent(categorias::add);
            }
            adicional.setCategorias(categorias);
        }
        Adicional guardado = adicionalRepository.save(adicional);
        return ResponseEntity.ok(guardado);
    }

    /**
     * UPDATE (Actualizar): Modifica los datos de un adicional existente.
     * Corresponde a AC35.
     * URL: PUT /api/adicionales/{id}
     */
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
                    adicionalExistente.setTipo(detallesAdicional.getTipo());
                    adicionalExistente.setImagen(detallesAdicional.getImagen());

                    // ✅ Actualizar categorías correctamente (buscándolas en la BD)
                    if (detallesAdicional.getCategorias() != null) {
                        Set<Categoria> nuevasCategorias = new HashSet<>();
                        for (Categoria cat : detallesAdicional.getCategorias()) {
                            Optional<Categoria> categoriaReal = categoriaRepository.findById(cat.getId());
                            categoriaReal.ifPresent(nuevasCategorias::add);
                        }
                        adicionalExistente.setCategorias(nuevasCategorias);
                    }

                    Adicional actualizado = adicionalRepository.save(adicionalExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE (Eliminar): Elimina un adicional de la base de datos.
     * Corresponde a AC36.
     * URL: DELETE /api/adicionales/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdicional(@PathVariable Long id) {
        if (!adicionalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        adicionalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{slug}")
    public ResponseEntity<List<Adicional>> getByCategoriaSlug(@PathVariable String slug) {
        // Debes implementar esta lógica en tu Repository o Service.
        // Asumiendo que tienes un método 'findByCategorias_Slug' en tu
        // AdicionalRepository:
        List<Adicional> adicionales = adicionalRepository.findByCategorias_Slug(slug);

        if (adicionales.isEmpty()) {
            // Si no encuentra, responde con OK (200) pero con lista vacía.
            // O puedes dejar que Spring lo maneje. Depende de cómo quieras el resultado.
            return ResponseEntity.ok(adicionales);
        }
        return ResponseEntity.ok(adicionales);
    }
}
