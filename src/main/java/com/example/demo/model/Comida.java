package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = true, length = 500)
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column
    private Boolean disponible = true;

    @Column
    private Integer tiempoPreparacion;

    @Column
    private Boolean esEspecialidad = false;

    @Column
    private String ingredientes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true) // ⚡ también lo dejo opcional
    private User user;

    // Relación con Adicional
    @ManyToMany
    @JoinTable(
        name = "comida_adicional",
        joinColumns = @JoinColumn(name = "comida_id"),
        inverseJoinColumns = @JoinColumn(name = "adicional_id")
    )
    @JsonIgnore   // 👈 evita loops
    private Set<Adicional> adicionales = new HashSet<>();

    // Constructor personalizado
    public Comida(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}
