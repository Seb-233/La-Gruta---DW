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
@Builder // ✅ Patrón Builder agregado
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @Builder.Default // ✅ Mantiene valor por defecto al usar Builder
    private Boolean activa = true;

    // 🔹 Relación con Comidas (una categoría puede tener varias comidas)
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Comida> comidas = new HashSet<>();

    // 🔹 Relación con Adicionales (muchos a muchos inversa)
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"categorias", "hibernateLazyInitializer", "handler"})
    @Builder.Default
    private Set<Adicional> adicionales = new HashSet<>();

    // 🔹 Relación auxiliar con AdicionalCategoria (tabla puente explícita)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<AdicionalCategoria> adicionalAsociaciones = new HashSet<>();

    // 🔹 Constructor útil para inicialización rápida (lo conservamos)
    public Categoria(String nombre, String slug) {
        this.nombre = nombre;
        this.slug = slug;
        this.activa = true;
    }
}
