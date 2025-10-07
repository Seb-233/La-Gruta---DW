package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Adicional;
import com.example.demo.repository.AdicionalRepository;

@Service
@Transactional
public class AdicionalServiceImpl implements AdicionalService {

    private final AdicionalRepository adicionalRepository;

    public AdicionalServiceImpl(AdicionalRepository adicionalRepository) {
        this.adicionalRepository = adicionalRepository;
    }

    @Override
    public List<Adicional> listarTodos() {
        return adicionalRepository.findAll();
    }

    @Override
    public Optional<Adicional> obtenerPorId(Long id) {
        return adicionalRepository.findById(id);
    }

    @Override
    public Adicional guardar(Adicional adicional) {
        return adicionalRepository.save(adicional);
    }

    @Override
    public void eliminar(Long id) {
        adicionalRepository.deleteById(id);
    }
}
