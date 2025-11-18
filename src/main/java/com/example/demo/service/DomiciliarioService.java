package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Domiciliario;
import com.example.demo.repository.DomiciliarioRepository;

@Service
public class DomiciliarioService {

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    public List<Domiciliario> getAll() {
        return domiciliarioRepository.findAll();
    }

    public Optional<Domiciliario> getById(Long id) {
        return domiciliarioRepository.findById(id);
    }

    public Domiciliario save(Domiciliario domiciliario) {
        return domiciliarioRepository.save(domiciliario);
    }

    public void delete(Long id) {
        domiciliarioRepository.deleteById(id);
    }
}
