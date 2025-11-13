package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operadores") // 🔹 (opcional) se recomienda usar plural en las tablas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder aplicado
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String password;
}
