package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

@Entity
@Table(name = "pedido_comida")
public class PedidoComida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
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
    ) // tabla intermedia para adicionales seleccionados
    private List<Adicional> adicionales;

    public PedidoComida() {}

    public PedidoComida(Pedido pedido, Comida comida, int cantidad) {
        this.pedido = pedido;
        this.comida = comida;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Comida getComida() { return comida; }
    public void setComida(Comida comida) { this.comida = comida; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public List<Adicional> getAdicionales() { return adicionales; }
    public void setAdicionales(List<Adicional> adicionales) { this.adicionales = adicionales; }
}
