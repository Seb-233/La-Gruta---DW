package com.example.demo.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "comidas")
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder agregado
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
    @Builder.Default // ✅ Mantiene el valor por defecto al usar el builder
    private Boolean disponible = true;

    @Column
    private Integer tiempoPreparacion;

    @Column(nullable = false)
    @Builder.Default
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
    @JsonIgnore
    @Builder.Default
    private Set<Adicional> adicionales = new HashSet<>();

    // 🔹 Relación inversa con PedidoComida
    @OneToMany(mappedBy = "comida", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PedidoComida> pedidosDondeAparece;

    // 🔹 Constructor personalizado (se mantiene para compatibilidad)
    public Comida(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}
