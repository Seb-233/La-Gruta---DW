package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comida;

@Repository
public interface ComidaRepository extends JpaRepository<Comida, Long> {

    // Buscar comidas por categoría
    List<Comida> findByCategoriaId(Long categoriaId);

    // Buscar comidas disponibles por categoría
    @Query("SELECT c FROM Comida c WHERE c.categoria.id = :categoriaId AND c.disponible = true")
    List<Comida> findDisponiblesByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Buscar comidas por nombre (búsqueda)
    @Query("SELECT c FROM Comida c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND c.disponible = true")
    List<Comida> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar especialidades
    @Query("SELECT c FROM Comida c WHERE c.esEspecialidad = true AND c.disponible = true")
    List<Comida> findEspecialidades();
}