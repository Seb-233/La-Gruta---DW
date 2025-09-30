package com.example.demo.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Comida;
import com.example.demo.repository.ComidaRepository;

@Service
public class ComidaServiceImpl implements ComidaService {

    @Autowired
    ComidaRepository repo;

    @Override
    public Comida searchById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Collection<Comida> searchAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void update(Comida comida) {
        repo.save(comida);
    }

    @Override
    public void add(Comida comida) {
        repo.save(comida);
    }
}
