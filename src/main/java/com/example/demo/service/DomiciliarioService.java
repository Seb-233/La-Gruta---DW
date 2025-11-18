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

    // ============================
    // 🔹 Obtener todos los domiciliarios
    // ============================
    public List<Domiciliario> getAll() {
        return domiciliarioRepository.findAll();
    }

    // ============================
    // 🔹 Obtener domiciliario por ID
    // ============================
    public Optional<Domiciliario> getById(Long id) {
        return domiciliarioRepository.findById(id);
    }

    // ============================
    // 🔹 Obtener domiciliarios disponibles
    // ============================
    public List<Domiciliario> getDisponibles() {
        return domiciliarioRepository.findByDisponibleTrue();
    }

    // ============================
    // 🔹 Crear un domiciliario
    // ============================
    public Domiciliario create(Domiciliario domiciliario) throws Exception {

        // Validar cédula única
        if (domiciliarioRepository.existsByCedula(domiciliario.getCedula())) {
            throw new Exception("La cédula ya existe");
        }

        return domiciliarioRepository.save(domiciliario);
    }

    // ============================
    // 🔹 Actualizar un domiciliario
    // ============================
    public Domiciliario update(Long id, Domiciliario data) throws Exception {

        Optional<Domiciliario> optional = domiciliarioRepository.findById(id);

        if (optional.isEmpty()) {
            throw new Exception("Domiciliario no encontrado");
        }

        Domiciliario dom = optional.get();

        dom.setNombre(data.getNombre());
        dom.setCorreo(data.getCorreo());
        dom.setTelefono(data.getTelefono());
        dom.setVehiculo(data.getVehiculo());
        dom.setPlaca(data.getPlaca());
        dom.setZonaCobertura(data.getZonaCobertura());
        dom.setDisponible(data.isDisponible());

        return domiciliarioRepository.save(dom);
    }

    // ============================
    // 🔹 Eliminar un domiciliario
    // ============================
    public void delete(Long id) throws Exception {
        if (!domiciliarioRepository.existsById(id)) {
            throw new Exception("Domiciliario no encontrado");
        }

        domiciliarioRepository.deleteById(id);
    }
}
