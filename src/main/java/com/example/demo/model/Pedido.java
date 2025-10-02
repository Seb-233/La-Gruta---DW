package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private User cliente;

    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;

    @ManyToOne
    private Domiciliario domiciliarioAsignado;

    // --- CAMPO 'total' AÑADIDO ---
    private Double total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PedidoComida> items;

    // --- GETTERS Y SETTERS ---

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

    // --- NUEVOS GETTER Y SETTER PARA 'total' ---
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}