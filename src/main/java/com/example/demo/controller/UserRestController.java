package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =====================================================
    // 🔹 POST: Registrar cliente completo (User + Cliente)
    // =====================================================
    @PostMapping(value = "/register-cliente", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerCliente(@RequestBody Cliente cliente) {

        User newUser = cliente.getUser();

        if (newUser == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta objeto user"));
        }

        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El username ya existe"));
        }

        // 🔥 ENCRIPTAR CONTRASEÑA
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // 🔥 SIEMPRE asignar rol CLIENTE
        Role rolCliente = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new RuntimeException("El rol CLIENTE no está creado en la base de datos"));

        newUser.setRoles(new HashSet<>());
        newUser.getRoles().add(rolCliente);

        // Guardar user
        User savedUser = userRepository.save(newUser);

        // Guardar cliente
        cliente.setUser(savedUser);
        clienteRepository.save(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Cliente registrado correctamente"));
    }
}
