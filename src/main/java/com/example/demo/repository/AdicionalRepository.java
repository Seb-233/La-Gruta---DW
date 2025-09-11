package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Adicional;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {

    @Query("SELECT a FROM Adicional a JOIN a.categorias c WHERE c.id = :categoriaId")
    List<Adicional> findByCategoriaId(Long categoriaId);


    @Query("SELECT a FROM Adicional a JOIN a.categorias c WHERE c.slug = :slug")
    List<Adicional> findByCategoriaSlug(@Param("slug") String slug);


    

    

}
