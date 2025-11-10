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
@Builder // ✅ Patrón Builder agregado
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
    @Builder.Default // ✅ Necesario para valores por defecto en Builder
    private Boolean disponible = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "categoria_adicional",
        joinColumns = @JoinColumn(name = "adicional_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "adicionales"})
    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    @OneToMany(mappedBy = "adicional", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<AdicionalCategoria> categoriaAsociaciones = new HashSet<>();

    @ManyToMany(mappedBy = "adicionales")
    @JsonIgnore
    @Builder.Default
    private Set<Comida> comidas = new HashSet<>();

    // Constructor adicional para compatibilidad con código existente
    public Adicional(String nombre, String descripcion, Double precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.disponible = true;
    }
}
