package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;

    private String estado;

    // Fecha de creación "real" en la DB
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;

    @ManyToOne
    private Domiciliario domiciliarioAsignado;

    // --- total del pedido ---
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY // mejor
                                                                                                            // LAZY para
                                                                                                            // evitar
                                                                                                            // cargas
                                                                                                            // pesadas
    )
    @JsonManagedReference
    private List<PedidoComida> items;

    // =========================
    // Ciclo de vida
    // =========================
    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "recibido"; // <-- Cambio aquí
        }
    }

    // =========================
    // Getters / Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCliente() {
        return cliente;
    }

    public void setCliente(User cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Domiciliario getDomiciliarioAsignado() {
        return domiciliarioAsignado;
    }

    public void setDomiciliarioAsignado(Domiciliario domiciliarioAsignado) {
        this.domiciliarioAsignado = domiciliarioAsignado;
    }

    public List<PedidoComida> getItems() {
        return items;
    }

    public void setItems(List<PedidoComida> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    // =========================
    // Alias de compatibilidad
    // (para el controlador que usa creadoEn)
    // =========================
    @Transient
    public LocalDateTime getCreadoEn() {
        return this.fechaCreacion;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.fechaCreacion = creadoEn;
    }
}
