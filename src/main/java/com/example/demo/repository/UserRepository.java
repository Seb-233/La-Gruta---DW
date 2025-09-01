package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // En sql la busqueda se haria asi -> select ¨from student where name = nombre
    
}
//http://localhost:8080/h2