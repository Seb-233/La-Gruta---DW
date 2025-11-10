package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedido_comida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder aplicado
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PedidoComida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonBackReference // evita recursión con Pedido.items
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comida_id")
    private Comida comida;

    private int cantidad;

    @ManyToMany
    @JoinTable(
        name = "pedido_comida_adicionales",
        joinColumns = @JoinColumn(name = "pedido_comida_id"),
        inverseJoinColumns = @JoinColumn(name = "adicional_id")
    )
    private List<Adicional> adicionales;
}
