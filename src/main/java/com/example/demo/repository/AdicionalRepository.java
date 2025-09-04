package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Adicional;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {

    // Lista por ID de categoría (JOIN por la propiedad "categoria")
    List<Adicional> findByCategoriaId(Long categoriaId);

    // Útil si quieres buscar por slug directamente
    List<Adicional> findByCategoriaSlug(String slug);
}
