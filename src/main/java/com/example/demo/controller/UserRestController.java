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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    // ============================
    // 🔹 GET: Todos los usuarios
    // ============================
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ============================
    // 🔹 GET: Usuario por ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        var optional = userRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        return ResponseEntity.ok(optional.get());
    }

    // ======================================================
    // 🔹 POST: Crear usuario (usado por tu front /register)
    // → Se le asigna el rol CLIENTE automáticamente
    // ======================================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> body) {

        String username = (String) body.get("username");
        String password = (String) body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos obligatorios"));
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El username ya existe"));
        }

        // Crear el usuario base
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Leer roles como array de strings
        List<String> roleNames = (List<String>) body.get("roles");

        if (roleNames == null || roleNames.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe incluir roles"));
        }

        // Convertir strings en entidades Role
        Set<Role> roles = new HashSet<>();

        for (String roleName : roleNames) {
            Role r = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no existe: " + roleName));
            roles.add(r);
        }

        user.setRoles(roles);

        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

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

        // 🔥 ENCRIPTAR CONTRASEÑA ANTES DE GUARDAR
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // Inicializar roles si vienen null
        if (newUser.getRoles() == null) {
            newUser.setRoles(new HashSet<>());
        }

        // Guardar usuario inicialmente
        newUser = userRepository.save(newUser);

        // Asignar rol CLIENTE
        Role rolCliente = roleRepository.findByName("CLIENTE").orElse(null);
        if (rolCliente != null) {
            newUser.getRoles().add(rolCliente);
            userRepository.save(newUser);
        }

        // Guardar cliente
        cliente.setUser(newUser);
        clienteRepository.save(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Cliente registrado correctamente"));
    }

    // ============================
    // 🔹 DELETE Usuario
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado"));
    }
}
