package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder aplicado
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;

    private String estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;

    @ManyToOne
    private Domiciliario domiciliarioAsignado;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default // ✅ Evita null al usar builder()
    private List<PedidoComida> items = new ArrayList<>();

    // =========================
    // Ciclo de vida
    // =========================
    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "EN_PROCESO";
        }
    }

    // =========================
    // Alias de compatibilidad (para DTO)
    // =========================
    @Transient
    public LocalDateTime getCreadoEn() {
        return this.fechaCreacion;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.fechaCreacion = creadoEn;
    }
}
