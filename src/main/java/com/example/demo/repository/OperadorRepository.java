package com.example.demo.repository;

import com.example.demo.model.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {

    // Buscar operador por user_id (cuando se loguea un operador)
    Optional<Operador> findByUserId(Long userId);

    // Validar si ya existe un operador relacionado al usuario
    boolean existsByUserId(Long userId);
}
