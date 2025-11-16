package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "domiciliarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Domiciliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, length = 20)
    private String celular;

    @Column(nullable = false)
    @Builder.Default
    private boolean disponible = true;
}
