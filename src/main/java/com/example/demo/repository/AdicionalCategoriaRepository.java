package com.example.demo.repository;

import com.example.demo.model.AdicionalCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdicionalCategoriaRepository extends JpaRepository<AdicionalCategoria, Long> {
    List<AdicionalCategoria> findByAdicionalId(Long adicionalId);

    List<AdicionalCategoria> findByCategoriaId(Long categoriaId);
}
