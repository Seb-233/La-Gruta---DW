package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "adicionales")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Adicional {

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

    @Column(nullable = false)
    private Boolean disponible = true;

    // 🔹 Relación muchos a muchos con Categoria (funcional para tu API actual)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "categoria_adicional",
        joinColumns = @JoinColumn(name = "adicional_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "adicionales"})
    private Set<Categoria> categorias = new HashSet<>();

    // 🔹 Relación auxiliar con AdicionalCategoria (no interfiere)
    @OneToMany(mappedBy = "adicional", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<AdicionalCategoria> categoriaAsociaciones = new HashSet<>();

    // 🔹 Relación inversa con Comida
    @ManyToMany(mappedBy = "adicionales")
    @JsonIgnore
    private Set<Comida> comidas = new HashSet<>();

    public Adicional(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}
