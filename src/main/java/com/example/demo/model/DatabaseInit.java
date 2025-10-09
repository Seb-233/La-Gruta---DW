package com.example.demo.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 🔹 Inicialización deshabilitada temporalmente
        System.out.println("⚙️ DatabaseInit ejecutado: Sin carga automática de datos.");
    }

}
