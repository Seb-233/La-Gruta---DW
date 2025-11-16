package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por username (login)
    Optional<User> findByUsername(String username);

    // Verificar si existe un usuario con ese username
    boolean existsByUsername(String username);
}
