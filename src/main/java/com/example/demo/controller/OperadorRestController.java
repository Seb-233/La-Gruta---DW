package com.example.demo.controller;

import com.example.demo.model.Operador;
import com.example.demo.model.User;
import com.example.demo.repository.OperadorRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/operadores", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OperadorRestController {

    private final OperadorRepository operadorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // ============================
    // 🔹 GET: Listar operadores
    // ============================
    @GetMapping
    public List<Operador> getAll() {
        return operadorRepository.findAll();
    }

    // ============================
    // 🔹 POST: Crear operador
    // ============================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Operador operador) {

        User u = operador.getUser();
        if (u == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta información del usuario"));
        }

        if (userRepository.existsByUsername(u.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre de usuario ya existe"));
        }

        // Guardar usuario
        User newUser = userRepository.save(u);

        // Asignar rol OPERADOR
        roleRepository.findByName("OPERADOR").ifPresent(rol -> {
            newUser.getRoles().add(rol);
            userRepository.save(newUser);
        });

        operador.setUser(newUser);
        operadorRepository.save(operador);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Operador creado correctamente"));
    }

    // ============================
    // 🔹 PUT: Actualizar operador
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Operador data) {
        return operadorRepository.findById(id)
                .map(op -> {
                    op.setNombre(data.getNombre());
                    operadorRepository.save(op);
                    return ResponseEntity.ok(Map.of("mensaje", "Operador actualizado"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Operador no encontrado")));
    }

    // ============================
    // 🔹 DELETE: Eliminar operador
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!operadorRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Operador no encontrado"));
        }

        operadorRepository.deleteById(id);

        return ResponseEntity.ok(Map.of("mensaje", "Operador eliminado"));
    }
}
