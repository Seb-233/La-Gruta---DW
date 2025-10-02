package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class PedidoComida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference // Evita bucles infinitos al convertir a JSON
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "comida_id")
    private Comida comida;

    // Campo para cumplir el requisito de la cantidad
    private int cantidad;

    // Campo para cumplir el requisito de los adicionales explícitos por plato
    @ManyToMany
    @JoinTable(name = "pedido_comida_adicionales", joinColumns = @JoinColumn(name = "pedido_comida_id"), inverseJoinColumns = @JoinColumn(name = "adicional_id"))
    private List<Adicional> adicionales;

    // --- Constructores, Getters y Setters ---

    public PedidoComida() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Comida getComida() {
        return comida;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<Adicional> getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(List<Adicional> adicionales) {
        this.adicionales = adicionales;
    }
}