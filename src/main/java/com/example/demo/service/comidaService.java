package com.example.demo.service;

import com.example.demo.model.comida;

import java.util.Collection;

public interface comidaService {

    public comida SearchById(Long id);

    public Collection<comida> SearchAll();

    public void deleteById(Long id);

    public void update(comida comida);

    public void add(comida comida);
}
