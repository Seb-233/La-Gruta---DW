/*package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // 🔹 Nombre de usuario único y obligatorio
    @Column(nullable = false, unique = true)
    private String username;

    // 🔹 Contraseña obligatoria
    @Column(nullable = false)
    private String password;

    // 🔹 Rol del usuario (como texto)
    // Si más adelante deseas tener una tabla Role separada, puedes cambiarlo a @ManyToOne
    @Column(nullable = false)
    private String role;

    // 🔹 Datos adicionales del usuario
    private String direccion;
    private String telefono;

    // 🔹 Relación con comidas
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comida> comidas = new ArrayList<>();
}
*/