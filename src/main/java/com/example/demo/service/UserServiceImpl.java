package com.example.demo.service;

import java.util.Collection;
import java.util.Optional;

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
    private PasswordEncoder passwordEncoder; // ✅ Encriptador

    @Override
    public User SearchById(Long id) {
        Optional<User> userOpt = repo.findById(id);
        return userOpt.orElse(null);
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
        if (user.getRole() != null) {
            Role role = roleRepo.findByName(user.getRole());
            user.setRoleEntity(role);
        }

        // ✅ Encriptar contraseña si no está encriptada
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        repo.save(user);
    }

    @Override
    public void add(User user) {
        if (user.getRole() != null) {
            Role role = roleRepo.findByName(user.getRole());
            user.setRoleEntity(role);
        }

        // ✅ Encriptar contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        repo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = repo.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
