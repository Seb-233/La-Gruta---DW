package com.example.demo.service;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User SearchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Collection<User> SearchAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void update(User user) {

        // -------------------------
        //   ACTUALIZAR ROLES
        // -------------------------
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {

            // Toma el primer rol enviado desde Angular
            Role requestedRole = user.getRoles().iterator().next();

            // Busca el rol en la base de datos
            Role roleEntity = roleRepo.findByName(requestedRole.getName()).orElse(null);

            if (roleEntity != null) {
                user.setRoles(Set.of(roleEntity));
            }
        }

        // -------------------------
        //  ENCRIPTAR PASSWORD SI ES NECESARIO
        // -------------------------
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        repo.save(user);
    }

    @Override
    public void add(User user) {

        // -------------------------
        //   ASIGNAR ROLES
        // -------------------------
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {

            Role requestedRole = user.getRoles().iterator().next();

            Role roleEntity = roleRepo.findByName(requestedRole.getName()).orElse(null);

            if (roleEntity != null) {
                user.setRoles(Set.of(roleEntity));
            }
        }

        // -------------------------
        //  ENCRIPTAR PASSWORD
        // -------------------------
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        repo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return repo.findByUsername(username).orElse(null); // ← CORREGIDO
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {

        User user = repo.findByUsername(username).orElse(null); // ← CORREGIDO

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }
}
