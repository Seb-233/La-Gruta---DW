package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por correo (útil para validar registro)
    Optional<Cliente> findByCorreo(String correo);

    // Buscar por id del User (útil cuando haces login)
    Optional<Cliente> findByUserId(Long userId);
}
