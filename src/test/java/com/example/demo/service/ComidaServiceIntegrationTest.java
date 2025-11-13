package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.model.Comida;
import com.example.demo.repository.ComidaRepository;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ComidaServiceIntegrationTest {

    @Autowired
    private ComidaService comidaService;

    @Autowired
    private ComidaRepository comidaRepository;

    private Comida comida1;
    private Comida comida2;

    @BeforeEach
    void setUp() {
        
        comidaRepository.deleteAll();

        comida1 = new Comida();
        comida1.setNombre("Lasagna Bolognesa");
        comida1.setDescripcion("Clásica lasaña italiana con carne y bechamel");
        comida1.setIngredientes("Carne molida, pasta, tomate, bechamel");
        comida1.setPrecio(34000.0);
        comida1.setDisponible(true);
        comida1.setEsEspecialidad(true);
        comida1.setImagen("https://tse2.mm.bing.net/th/id/OIP.Lasagna?pid=Api");

        comida2 = new Comida();
        comida2.setNombre("Risotto ai Funghi");
        comida2.setDescripcion("Risotto cremoso con champiñones portobello");
        comida2.setIngredientes("Arroz arbóreo, champiñones, parmesano, vino blanco");
        comida2.setPrecio(31000.0);
        comida2.setDisponible(true);
        comida2.setEsEspecialidad(false);
        comida2.setImagen("https://tse2.mm.bing.net/th/id/OIP.Risotto?pid=Api");

        comidaService.add(comida1);
        comidaService.add(comida2);
    }

    @Test
    void debeListarTodasLasComidas() {
        List<Comida> comidas = (List<Comida>) comidaService.searchAll();
        assertNotNull(comidas);
        assertEquals(2, comidas.size());
    }

    @Test
    void debeCrearYBuscarComidaPorId() {
        Comida nueva = new Comida();
        nueva.setNombre("Fettuccine Alfredo");
        nueva.setDescripcion("Pasta con salsa cremosa de parmesano y mantequilla");
        nueva.setIngredientes("Fettuccine, parmesano, mantequilla, crema");
        nueva.setPrecio(29000.0);
        nueva.setDisponible(true);
        nueva.setEsEspecialidad(false);
        nueva.setImagen("https://tse3.mm.bing.net/th/id/OIP.Fettuccine?pid=Api");

        comidaService.add(nueva);

        Comida encontrada = comidaService.searchById(nueva.getId());
        assertNotNull(encontrada);
        assertEquals("Fettuccine Alfredo", encontrada.getNombre());
    }

    @Test
    void debeActualizarComida() {
        comida1.setPrecio(36000.0);
        comidaService.update(comida1);

        Comida actualizada = comidaService.searchById(comida1.getId());
        assertNotNull(actualizada);
        assertEquals(36000.0, actualizada.getPrecio());
    }

    @Test
    void debeEliminarComidaPorId() {
        comidaService.deleteById(comida2.getId());
        assertThrows(NoSuchElementException.class, () -> comidaService.searchById(comida2.getId()));
    }
}