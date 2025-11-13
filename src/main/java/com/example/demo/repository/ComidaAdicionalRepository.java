package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ComidaAdicional;

import jakarta.transaction.Transactional;

@Repository
public interface ComidaAdicionalRepository extends JpaRepository<ComidaAdicional, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ComidaAdicional ca WHERE ca.adicionalId = :adicionalId")
    void deleteByAdicionalId(Long adicionalId);
}
