package com.example.demo.controller;

import java.util.Collection;

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

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ LOGIN (único y correcto)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validar campos
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Debe enviar username y password");
        }

        // Buscar usuario por nombre y contraseña
        User user = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (user == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        // Retornar respuesta con datos seguros
        LoginResponse response = new LoginResponse(user.getId(), user.getUsername(), user.getRole());
        return ResponseEntity.ok(response);
    }

    // ✅ GET ALL
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.SearchAll();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.SearchById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        userService.add(user);
        return ResponseEntity.ok(user);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = userService.SearchById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setDireccion(updatedUser.getDireccion());
        existingUser.setTelefono(updatedUser.getTelefono());

        userService.update(existingUser);
        return ResponseEntity.ok(existingUser);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
