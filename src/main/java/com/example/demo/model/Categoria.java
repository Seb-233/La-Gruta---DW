package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 500)
    private String descripcion;

    @Column
    private String imagen;

    @Column
    private Integer orden;

    @Column
    private Boolean activa = true;

    // 🔹 Relación con Comidas
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private Set<Comida> comidas = new HashSet<>();

    // 🔹 Relación con Adicionales (para tu API actual)
    @ManyToMany(mappedBy = "categorias")
    @JsonIgnoreProperties("categorias")
    private Set<Adicional> adicionales = new HashSet<>();

    // 🔹 Relación auxiliar con AdicionalCategoria (no interfiere)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<AdicionalCategoria> adicionalAsociaciones = new HashSet<>();

    public Categoria(String nombre, String slug) {
        this.nombre = nombre;
        this.slug = slug;
        this.activa = true;
    }
}
