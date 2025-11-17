package com.example.demo.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import com.example.demo.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // -------------------------
    //        LOGIN
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Debe enviar username y password");
        }

        User user = userService.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (user == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        // Convertimos el Set<Role> a una lista de strings
        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        // Tomamos el primer rol para el JWT (si tu JWT soporta varios, me avisas)
        String mainRole = roles.isEmpty() ? "USER" : roles.get(0);

        String token = jwtUtil.generateToken(user.getUsername(), mainRole);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("roles", roles);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // -------------------------
    //     GET ALL USERS
    // -------------------------
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.SearchAll();
    }

    // -------------------------
    // GET USERS BY ROLE (nuevo)
    // -------------------------
    @GetMapping("/role/{name}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String name) {
        List<User> users = userRepository.findByRoles_Name(name);
        return ResponseEntity.ok(users);
    }

    // -------------------------
    //      GET USER BY ID
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.SearchById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // -------------------------
    //        CREATE USER
    // -------------------------
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        // Validación mínima
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        userService.add(user);
        return ResponseEntity.ok(user);
    }

    // -------------------------
    //        UPDATE USER
    // -------------------------
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

        User existingUser = userService.SearchById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRoles(updatedUser.getRoles());

        userService.update(existingUser);
        return ResponseEntity.ok(existingUser);
    }

    // -------------------------
    //        DELETE USER
    // -------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
