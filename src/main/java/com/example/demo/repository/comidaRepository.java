package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.comida;

@Repository
public interface comidaRepository extends JpaRepository<comida, Long> {

    // Buscar comidas por categoría
    List<comida> findByCategoriaId(Long categoriaId);

    // Buscar comidas disponibles por categoría
    @Query("SELECT c FROM comida c WHERE c.categoria.id = :categoriaId AND c.disponible = true")
    List<comida> findDisponiblesByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Buscar comidas por nombre (búsqueda)
    @Query("SELECT c FROM comida c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.disponible = true")
    List<comida> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar especialidades
    @Query("SELECT c FROM comida c WHERE c.esEspecialidad = true AND c.disponible = true")
    List<comida> findEspecialidades();
}