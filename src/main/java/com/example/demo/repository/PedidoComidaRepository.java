package com.example.demo.repository;

import com.example.demo.model.PedidoComida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoComidaRepository extends JpaRepository<PedidoComida, Long> {
}