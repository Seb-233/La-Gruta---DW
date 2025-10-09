package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categorias")
@Data
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
    private String slug; // Para URLs amigables (ej: "antipastos", "pizzas")

    @Column(length = 500)
    private String descripcion;

    @Column
    private String imagen; // URL o path de la imagen de la categoría

    @Column
    private Integer orden; // Para ordenar las categorías en el menú

    @Column
    private Boolean activa = true; // Para activar/desactivar categorías

    // Relación inversa con Comida
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore // 👈 evita loops
    private Set<Comida> comidas = new HashSet<>();

    // Relación inversa con Adicional
    @ManyToMany(mappedBy = "categorias")
    @JsonIgnoreProperties("categorias") // 👈 evita recursión desde Adicional
    private Set<Adicional> adicionales = new HashSet<>();

    // Constructor personalizado
    public Categoria(String nombre, String slug) {
        this.nombre = nombre;
        this.slug = slug;
        this.activa = true;
    }
}