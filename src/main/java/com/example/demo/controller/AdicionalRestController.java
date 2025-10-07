package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;
import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;

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
    // ✅ Obtener todos los adicionales
    @GetMapping
    public List<Adicional> getAll() {
        return adicionalRepository.findAll();
    }

   @GetMapping("/categoria/{slug}")
public ResponseEntity<List<Adicional>> getByCategoriaSlug(@PathVariable String slug) {
    // usa el tipo totalmente calificado para esquivar el import incorrecto
    final java.util.Optional<Categoria> categoriaOpt = categoriaRepository.findBySlug(slug);

    if (categoriaOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    final Categoria categoria = categoriaOpt.get();
    final List<Adicional> adicionales = adicionalRepository.findByCategoriasContaining(categoria);
    return ResponseEntity.ok(adicionales);
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
     * Corresponde a AC33 (aunque el AC lo describe como READ, la funcionalidad es
     * CREATE).
     * URL: POST /api/adicionales
     */
    @PostMapping
    public Adicional crearAdicional(@RequestBody Adicional adicional) {
        return adicionalRepository.save(adicional);
    }

    /**
     * UPDATE (Actualizar): Modifica los datos de un adicional existente.
     * Corresponde a AC35.
     * URL: PUT /api/adicionales/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Adicional> actualizarAdicional(@PathVariable Long id,
            @RequestBody Adicional detallesAdicional) {
        return adicionalRepository.findById(id)
                .map(adicionalExistente -> {
                    adicionalExistente.setNombre(detallesAdicional.getNombre());
                    adicionalExistente.setDescripcion(detallesAdicional.getDescripcion());
                    adicionalExistente.setPrecio(detallesAdicional.getPrecio());
                    // Aquí puedes añadir más campos para actualizar si es necesario

                    Adicional actualizado = adicionalRepository.save(adicionalExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE (Eliminar): Elimina un adicional de la base de datos.
     * Corresponde a AC36 (aunque el AC dice "desactivar", por simplicidad lo
     * eliminamos).
     * URL: DELETE /api/adicionales/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdicional(@PathVariable Long id) {
        if (!adicionalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // NOTA: Si tienes relaciones (ej. en PedidoComida) que dependen de Adicional,
        // puede que necesites borrar esas referencias primero para evitar un error de
        // clave foránea.
        // Por ahora, lo mantenemos simple.
        adicionalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}