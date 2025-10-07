package com.example.demo.controller;

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adicionales")
@CrossOrigin(origins = "http://localhost:4200") // Permite la conexión con Angular
public class AdicionalRestController {

    @Autowired
    private AdicionalRepository adicionalRepository;

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