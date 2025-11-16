package com.example.demo.repository;

import com.example.demo.model.Domiciliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Long> {

    // Obtener todos los domiciliarios disponibles
    List<Domiciliario> findByDisponibleTrue();

    // Para buscar por cédula (única)
    boolean existsByCedula(String cedula);
}
