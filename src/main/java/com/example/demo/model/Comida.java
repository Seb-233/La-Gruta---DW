package com.example.demo.model;

import java.util.HashSet;
import java.util.List;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "comidas")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(length = 500)
    private String imagen;

    // 🔹 Cada comida pertenece a una categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Column
    private Integer tiempoPreparacion;

    @Column(nullable = false)
    private Boolean esEspecialidad = false;

    @Column(length = 1000)
    private String ingredientes;

    // 🔹 Relación con el usuario que la creó (si aplica)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // 🔹 Relación muchos a muchos con Adicionales
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "comida_adicional",
        joinColumns = @JoinColumn(name = "comida_id"),
        inverseJoinColumns = @JoinColumn(name = "adicional_id")
    )
    @JsonIgnore // evita bucles JSON entre comida ↔ adicional
    private Set<Adicional> adicionales = new HashSet<>();

    // 🔹 Relación inversa con PedidoComida (una comida puede aparecer en muchos pedidos)
    @OneToMany(mappedBy = "comida", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // evita bucles infinitos en la serialización
    private List<PedidoComida> pedidosDondeAparece;

    // 🔹 Constructor personalizado
    public Comida(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}
