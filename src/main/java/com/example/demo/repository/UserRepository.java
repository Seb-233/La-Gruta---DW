package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // En sql la busqueda se haria asi -> select ¨from student where name = nombre
    Optional<User> findByUsernameAndPassword(String username, String password);

    
}
//http://localhost:8080/h2