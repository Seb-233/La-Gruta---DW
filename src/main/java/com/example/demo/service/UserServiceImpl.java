package com.example.demo.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
        repo.save(user);
    }

    @Override
    public void add(User user) {
        // 🔒 Encriptar contraseña antes de guardar
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
}
