package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Adicional;
import com.example.demo.model.AdicionalCategoria;
import com.example.demo.model.Categoria;

@Repository
public interface AdicionalCategoriaRepository extends JpaRepository<AdicionalCategoria, Long> {

    // 🔹 Busca las asociaciones por entidad Adicional
    List<AdicionalCategoria> findByAdicional(Adicional adicional);

    // 🔹 Busca las asociaciones por entidad Categoria
    List<AdicionalCategoria> findByCategoria(Categoria categoria);

    // 🔹 Busca las asociaciones por ID de Adicional
    List<AdicionalCategoria> findByAdicionalId(Long adicionalId);

    // 🔹 Busca las asociaciones por ID de Categoria
    List<AdicionalCategoria> findByCategoriaId(Long categoriaId);
}
