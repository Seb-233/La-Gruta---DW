package com.example.demo.dto; // o package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;
import com.example.demo.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Faltan credenciales");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Usuario o contraseña inválidos");
        }

        // IMPORTANT: si guardaste contraseñas en texto plano antes, ajusta esto.
        // Aquí asumimos que passwords están encriptadas con passwordEncoder.
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            return ResponseEntity.status(401).body("Usuario o contraseña inválidos");
        }

        // Convertir roles a strings (tu User tiene Set<Role>)
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // toma el primer rol para el token (puedes ajustar si quieres múltiples claims)
        String roleForToken = roleNames.stream().findFirst().orElse("CLIENTE");

        String token = jwtUtil.generateToken(user.getUsername(), roleForToken);

        LoginResponse res = new LoginResponse(user.getId(), user.getUsername(), roleNames, token);
        return ResponseEntity.ok(res);
    }
}
