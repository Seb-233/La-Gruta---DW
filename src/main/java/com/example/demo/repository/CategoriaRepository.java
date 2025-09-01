package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar categoría por slug
    Optional<Categoria> findBySlug(String slug);
    
    // Buscar categorías activas ordenadas
    @Query("SELECT c FROM Categoria c WHERE c.activa = true ORDER BY c.orden ASC, c.nombre ASC")
    List<Categoria> findActivasOrdenadas();
    
    // Verificar si existe slug
    boolean existsBySlug(String slug);
}