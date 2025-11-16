package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteRestController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserRepository userRepository; // (No se usa pero no afecta)

    // ============================
    // 🔹 GET: Todos los clientes
    // ============================
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // ============================
    // 🔹 GET: Cliente por ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {

        var optional = clienteRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado"));
        }

        return ResponseEntity.ok(optional.get());
    }

    // ====================================================
    // 🔹 GET: Cliente por UserId
    // ====================================================
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getClienteByUser(@PathVariable Long userId) {

        var optional = clienteRepository.findByUserId(userId);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado"));
        }

        return ResponseEntity.ok(optional.get());
    }

    // ============================
    // 🔹 DELETE Cliente
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {

        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Cliente no encontrado"));
        }

        clienteRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente eliminado correctamente"));
    }
}
