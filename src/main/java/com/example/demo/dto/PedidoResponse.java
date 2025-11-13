package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    public Long id;
    public String estado;
    public Double total;
    public LocalDateTime creadoEn;
    public List<Item> items;

    public static class Item {
        public Long pedidoComidaId;
        public Long comidaId;
        public String comidaNombre;
        public int cantidad;
        public List<AdicionalRes> adicionales;
        public Double subtotal;
    }

    public static class AdicionalRes {
        public Long id;
        public String nombre;
        public Double precio; // déjalo null si Adicional no maneja precio
    }
}
