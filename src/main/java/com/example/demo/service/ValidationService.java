package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class ValidationService {

    @Autowired
    private UserRepository userRepository;

    public boolean validateUser(String username, String password) {

        // findByUsername devuelve Optional<User>, así que debemos resolverlo
        User user = userRepository.findByUsername(username).orElse(null);

        return user != null && password.equals(user.getPassword());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

}
