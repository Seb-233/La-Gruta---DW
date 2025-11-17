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

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/operadores", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OperadorRestController {

    private final OperadorRepository operadorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // ✅ necesario para encriptar

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
            return ResponseEntity
                    .badRequest()
                    .body("Falta información del usuario");
        }

        if (userRepository.existsByUsername(u.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("El nombre de usuario ya existe");
        }

        // 🔐 Encriptar contraseña ANTES de guardar
        u.setPassword(passwordEncoder.encode(u.getPassword()));

        // Guardar usuario
        User newUser = userRepository.save(u);

        // Asignar rol OPERADOR
        roleRepository.findByName("OPERADOR").ifPresent(rol -> {
            newUser.getRoles().add(rol);
            userRepository.save(newUser);
        });

        // Asociar el User al operador
        operador.setUser(newUser);

        // Guardar operador
        Operador saved = operadorRepository.save(operador);

        // 🔥 Devolver el operador completo (compatible con Angular)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ============================
    // 🔹 PUT: Actualizar operador
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Operador data) {

        var optional = operadorRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Operador no encontrado");
        }

        Operador op = optional.get();
        op.setNombre(data.getNombre());
        operadorRepository.save(op);

        return ResponseEntity.ok(op);
    }

    // ============================
    // 🔹 DELETE: Eliminar operador
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!operadorRepository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Operador no encontrado");
        }

        operadorRepository.deleteById(id);

        return ResponseEntity.ok("Operador eliminado");
    }
}
