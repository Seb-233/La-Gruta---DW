package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS_TABLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Aplica el patrón Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ← obligatorio

    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default // ✅ Evita NullPointerException al usar el builder
    private List<Comida> comidas = new ArrayList<>();
}
