package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean activa = true;

    // 🔹 Relación con Comidas (una categoría puede tener varias comidas)
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore // evita loops JSON con Comida
    private Set<Comida> comidas = new HashSet<>();

    // 🔹 Relación con Adicionales (muchos a muchos inversa)
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"categorias", "hibernateLazyInitializer", "handler"})
    private Set<Adicional> adicionales = new HashSet<>();

    // 🔹 Relación auxiliar con AdicionalCategoria (tabla puente explícita)
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<AdicionalCategoria> adicionalAsociaciones = new HashSet<>();

    // 🔹 Constructor útil para inicialización rápida
    public Categoria(String nombre, String slug) {
        this.nombre = nombre;
        this.slug = slug;
        this.activa = true;
    }
}
