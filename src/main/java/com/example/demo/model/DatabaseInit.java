package com.example.demo.model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.repository.AdicionalRepository;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ComidaRepository;
import com.example.demo.repository.DomiciliarioRepository;
import com.example.demo.repository.OperadorRepository;
import com.example.demo.repository.PedidoComidaRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.UserRepository;

@Component
@Transactional
public class DatabaseInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DomiciliarioRepository domiciliarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ComidaRepository comidaRepository;
    private final AdicionalRepository adicionalRepository;
    private final OperadorRepository operadorRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoComidaRepository pedidoComidaRepository;

    // ✅ Constructor para inyección de dependencias (Spring Boot)
    public DatabaseInit(UserRepository userRepository,
                        DomiciliarioRepository domiciliarioRepository,
                        CategoriaRepository categoriaRepository,
                        ComidaRepository comidaRepository,
                        AdicionalRepository adicionalRepository,
                        OperadorRepository operadorRepository,
                        PedidoRepository pedidoRepository,
                        PedidoComidaRepository pedidoComidaRepository) {
        this.userRepository = userRepository;
        this.domiciliarioRepository = domiciliarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.comidaRepository = comidaRepository;
        this.adicionalRepository = adicionalRepository;
        this.operadorRepository = operadorRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoComidaRepository = pedidoComidaRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean ENABLE_SEED = true; // ⚙️ cambia a false si no quieres datos iniciales

        if (!ENABLE_SEED) {
            System.out.println("⚙️ DatabaseInit ejecutado: Sin carga automática de datos.");
            return;
        }
    }

}
