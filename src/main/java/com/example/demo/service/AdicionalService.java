package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Adicional;

public interface AdicionalService {
    List<Adicional> listarTodos();
    Optional<Adicional> obtenerPorId(Long id);
    Adicional guardar(Adicional adicional);
    void eliminar(Long id);
}
