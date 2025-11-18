package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.model.Cliente;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping(value = "/api/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteRestController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //@Autowired
    //private UserRepository userRepository; // (No se usa pero no afecta)

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

    // ============================
// 🔹 PUT: Actualizar Cliente
// ============================
@PutMapping("/{id}")
public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteUpdate) {

    var optional = clienteRepository.findById(id);

    if (optional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Cliente no encontrado"));
    }

    Cliente cliente = optional.get();

    cliente.setNombre(clienteUpdate.getNombre());
    cliente.setApellido(clienteUpdate.getApellido());
    cliente.setCorreo(clienteUpdate.getCorreo());
    cliente.setTelefono(clienteUpdate.getTelefono());
    cliente.setDireccion(clienteUpdate.getDireccion());

    clienteRepository.save(cliente);

    return ResponseEntity.ok(cliente);
}

@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {

    if (cliente.getUser() == null) {
        return ResponseEntity.badRequest().body("Debe enviar user dentro de cliente");
    }

    User u = cliente.getUser();

    if (u.getUsername() == null || u.getPassword() == null) {
        return ResponseEntity.badRequest().body("Username y password son obligatorios");
    }

    if (userRepository.existsByUsername(u.getUsername())) {
        return ResponseEntity.badRequest().body("El username ya existe");
    }

    // encriptar password
    u.setPassword(passwordEncoder.encode(u.getPassword()));

    // asignar rol CLIENTE
    Role rolCliente = roleRepository.findByName("CLIENTE")
        .orElseThrow(() -> new RuntimeException("Rol CLIENTE no existe"));

    u.setRoles(Set.of(rolCliente));

    // guardar usuario
    User savedUser = userRepository.save(u);

    // asociar user al cliente
    cliente.setUser(savedUser);

    // guardar cliente
    Cliente saved = clienteRepository.save(cliente);

    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}



}
