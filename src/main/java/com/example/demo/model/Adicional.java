package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adicionales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column
    private String imagen;

    @Column
    private Boolean disponible = true;

    @Column
    private String tipo;

    // Relación con Categoria
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "adicional_categoria", joinColumns = @JoinColumn(name = "adicional_id"), inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    @JsonIgnore // 👈 evita loops
    private Set<Categoria> categorias = new HashSet<>();

    // Relación con Comida
    @ManyToMany(mappedBy = "adicionales")
    @JsonIgnore // 👈 evita loops
    private Set<Comida> comidas = new HashSet<>();

    // Constructor personalizado
    public Adicional(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}