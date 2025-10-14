package com.example.demo.dto;

import java.util.List;

public class CreatePedidoRequest {
    public Long clienteId;          // opcional
    public String direccion;        // opcional
    public String notas;            // opcional
    public List<Item> items;

    public static class Item {
        public Long comidaId;
        public int cantidad;
        public List<Long> adicionalIds; // puede ser null o []
    }
}
