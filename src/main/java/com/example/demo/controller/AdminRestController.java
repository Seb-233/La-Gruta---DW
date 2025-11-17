package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/admins", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
public class AdminRestController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LISTAR TODOS LOS ADMINS
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // OBTENER ADMIN POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        var opt = adminRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Administrador no encontrado"));
        }
        return ResponseEntity.ok(opt.get());
    }

    // CREAR ADMIN
    // Puede recibir un Admin con user (new user data) o un admin que referencia a un user existente (user.id)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        if (admin == null || admin.getUser() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta objeto user en el body"));
        }

        User incomingUser = admin.getUser();

        // Si viene un username existente -> no permitir duplicados
        if (incomingUser.getUsername() == null || incomingUser.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "user.username y user.password son obligatorios"));
        }

        if (userRepository.existsByUsername(incomingUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El username ya existe"));
        }

        // Encriptar password
        incomingUser.setPassword(passwordEncoder.encode(incomingUser.getPassword()));

        // Asignar rol ADMIN al user
        Role rolAdmin = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("El rol ADMIN no está creado en la base de datos"));

        incomingUser.setRoles(new HashSet<>());
        incomingUser.getRoles().add(rolAdmin);

        // Guardar user
        User savedUser = userRepository.save(incomingUser);

        // Guardar admin
        admin.setUser(savedUser);
        Admin savedAdmin = adminRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }

    // ACTUALIZAR ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        var opt = adminRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Administrador no encontrado"));
        }

        Admin existing = opt.get();
        existing.setNombre(updatedAdmin.getNombre());
        existing.setApellido(updatedAdmin.getApellido());
        existing.setCorreo(updatedAdmin.getCorreo());
        existing.setTelefono(updatedAdmin.getTelefono());
        existing.setDireccion(updatedAdmin.getDireccion());

        // Si se actualiza el usuario asociado:
        if (updatedAdmin.getUser() != null) {
            User u = existing.getUser();

            // Update username only if different and not already used
            String newUsername = updatedAdmin.getUser().getUsername();
            if (newUsername != null && !newUsername.equals(u.getUsername())) {
                if (userRepository.existsByUsername(newUsername)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "El username ya existe"));
                }
                u.setUsername(newUsername);
            }

            // Update password if provided (encode)
            String rawPass = updatedAdmin.getUser().getPassword();
            if (rawPass != null && !rawPass.isBlank()) {
                u.setPassword(passwordEncoder.encode(rawPass));
            }

            userRepository.save(u);
            existing.setUser(u);
        }

        Admin saved = adminRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    // ELIMINAR ADMIN (y opcionalmente quitar rol o borrar user si así lo deseas)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        var opt = adminRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Administrador no encontrado"));
        }

        Admin admin = opt.get();
        Long userId = admin.getUser().getId();

        // Borrar la entidad admin
        adminRepository.deleteById(id);

        // Opcional: quitar rol ADMIN del usuario y/o borrar usuario.
        // Aquí dejaré la opción: quitar rol ADMIN (si queda sin roles podrías eliminar)
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
            if (adminRole != null && user.getRoles() != null) {
                user.getRoles().removeIf(r -> "ADMIN".equals(r.getName()));
                userRepository.save(user);
            }
        }

        return ResponseEntity.ok(Map.of("mensaje", "Administrador eliminado correctamente"));
    }
}
