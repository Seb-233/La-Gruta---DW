
package com.example.demo.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@CrossOrigin(origins = "http://localhost:4200") // Permite conexión con Angular
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.SearchAll();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.SearchById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Crear usuario nuevo
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        userService.add(user);
        return ResponseEntity.ok(user);
    }

    // Actualizar usuario
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

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

