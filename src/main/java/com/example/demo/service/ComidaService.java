package com.example.demo.service;

import com.example.demo.model.Comida;

import java.util.Collection;

public interface ComidaService {

    public Comida searchById(Long id);

    public Collection<Comida> searchAll();

    public void deleteById(Long id);

    public void update(Comida comida);

    public void add(Comida comida);
}
