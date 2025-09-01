package com.example.demo.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.comida;
import com.example.demo.repository.comidaRepository;

@Service
public class comidaServiceImpl implements comidaService {

    @Autowired
    comidaRepository repo;

    @Override
    public comida SearchById(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public Collection<comida> SearchAll() {
        return repo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void update(comida comida) {
        repo.save(comida);
    }

    @Override
    public void add(comida comida) {
        repo.save(comida);
    }
}
