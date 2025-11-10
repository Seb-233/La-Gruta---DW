package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domiciliarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder aplicado
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
    @Builder.Default
    private boolean disponible = true; // true si está disponible para pedidos

    @Column
    private String zonaCobertura; // zona o ciudad donde opera
}
