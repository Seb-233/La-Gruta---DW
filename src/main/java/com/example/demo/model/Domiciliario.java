package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "domiciliarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domiciliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String vehiculo; // Ej: Moto, Bicicleta, Carro

    @Column
    private String placa; // opcional, solo aplica para moto/carro

    @Column(nullable = false)
    private boolean disponible = true; // true si está disponible para pedidos

    @Column
    private String zonaCobertura; // zona o ciudad donde opera
}
