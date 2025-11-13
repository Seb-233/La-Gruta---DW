package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Adicional;
import com.example.demo.model.Categoria;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {

    // 🔹 Consulta personalizada por ID de categoría
    @Query("SELECT a FROM Adicional a JOIN a.categorias c WHERE c.id = :categoriaId")
    List<Adicional> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    // 🔹 Consulta personalizada por slug de categoría
    @Query("SELECT a FROM Adicional a JOIN a.categorias c WHERE c.slug = :slug")
    List<Adicional> findByCategoriaSlug(@Param("slug") String slug);

    // 🔹 Buscar adicional por nombre exacto
    Optional<Adicional> findByNombre(String nombre);

    // 🔹 Buscar adicionales que contengan una categoría específica
    List<Adicional> findByCategoriasContaining(Categoria categoria);

    // 🔹 Buscar adicionales asociados al slug de una categoría
    List<Adicional> findByCategorias_Slug(String slug);
}
