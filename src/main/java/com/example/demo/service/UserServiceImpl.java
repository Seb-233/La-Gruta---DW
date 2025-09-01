package com.example.demo.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;



@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public User SearchById(Long id) {
        return repo.findById(id).get();
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
        repo.save(user);
    }

    @Override
    public User findByUsername(String username) {
    return repo.findByUsername(username);
    }


}
