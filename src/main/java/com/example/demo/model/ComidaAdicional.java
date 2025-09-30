package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comida_adicional")
public class ComidaAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comida_id")
    private Long comidaId;

    @Column(name = "adicional_id")
    private Long adicionalId;

    // getters y setters
}
