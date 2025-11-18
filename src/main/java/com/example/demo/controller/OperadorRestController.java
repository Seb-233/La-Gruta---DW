package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Operador;
import com.example.demo.model.User;
import com.example.demo.repository.OperadorRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/operadores", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class OperadorRestController {

    private final OperadorRepository operadorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // LISTAR TODOS
    @GetMapping
    public List<Operador> getAll() {
        return operadorRepository.findAll();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        var opt = operadorRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Operador no encontrado"));
        }
        return ResponseEntity.ok(opt.get());
    }

    // CREAR OPERADOR
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Operador operador) {
        if (operador == null || operador.getUser() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta información del usuario"));
        }

        User u = operador.getUser();

        if (u.getUsername() == null || u.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username y password son obligatorios"));
        }

        if (userRepository.existsByUsername(u.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El username ya existe"));
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));

        // Asignar rol OPERADOR
        roleRepository.findByName("OPERADOR").ifPresent(r -> u.getRoles().add(r));

        User savedUser = userRepository.save(u);
        operador.setUser(savedUser);

        Operador saved = operadorRepository.save(operador);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ACTUALIZAR OPERADOR
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Operador data) {
        var opt = operadorRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Operador no encontrado"));
        }

        Operador existing = opt.get();
        existing.setNombre(data.getNombre());
        existing.setApellido(data.getApellido());
        existing.setCorreo(data.getCorreo());
        existing.setTelefono(data.getTelefono());

        // Actualizar usuario asociado
        if (data.getUser() != null) {
            User u = existing.getUser();
            if (data.getUser().getUsername() != null && !data.getUser().getUsername().equals(u.getUsername())) {
                if (userRepository.existsByUsername(data.getUser().getUsername())) {
                    return ResponseEntity.badRequest().body(Map.of("error", "El username ya existe"));
                }
                u.setUsername(data.getUser().getUsername());
            }
            if (data.getUser().getPassword() != null && !data.getUser().getPassword().isBlank()) {
                u.setPassword(passwordEncoder.encode(data.getUser().getPassword()));
            }
            userRepository.save(u);
            existing.setUser(u);
        }

        Operador saved = operadorRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ELIMINAR OPERADOR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var opt = operadorRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Operador no encontrado"));
        }

        Operador op = opt.get();
        Long userId = op.getUser().getId();

        operadorRepository.deleteById(id);

        // Quitar rol OPERADOR del user si quieres
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            roleRepository.findByName("OPERADOR").ifPresent(r -> user.getRoles().removeIf(role -> role.getName().equals("OPERADOR")));
            userRepository.save(user);
        }

        return ResponseEntity.ok(Map.of("mensaje", "Operador eliminado correctamente"));
    }
}
