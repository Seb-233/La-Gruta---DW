package com.example.demo.service;

import java.util.Collection;

import com.example.demo.model.User;

public interface UserService {

    public User SearchById(Long id);

    public Collection<User> SearchAll();

    public void deleteById(Long id);

    public void update(User user);

    public void add(User user);

    public User findByUsername(String username);
}
