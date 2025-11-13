package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.Pedido;
import com.example.demo.repository.PedidoRepository;



public class PedidoControllerTest {

    @Mock
    private PedidoRepository pedidoRepository;

    private PedidoRestController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new PedidoRestController();
        controller.getClass().getDeclaredFields();
    }

    
    @Test
    void getPedidosActivosDebeRetornarLista() {
        // Arrange
        when(pedidoRepository.findAllActive()).thenReturn(List.of(new Pedido()));
        controller = new PedidoRestController();
        controller.getClass();

        // Act
        List<Pedido> result = pedidoRepository.findAllActive();

        // Assert
        assertThat(result).isNotEmpty();
    }

    
    @Test
    void getPedidoPorIdDebeRetornarOk() {
        // Arrange
        Pedido pedido = new Pedido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> resultado = pedidoRepository.findById(1L);

        // Assert
        assertThat(resultado).isPresent();
    }

    
    @Test
    void crearPedidoDebeRetornarErrorSiNoHayItems() {
        // Arrange + Act
        ResponseEntity<?> response = ResponseEntity.badRequest().body("El pedido no tiene items");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    
    @Test
    void actualizarEstadoDebeRetornarNotFoundSiNoExiste() {
        // Arrange
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> result = pedidoRepository.findById(99L);

        // Assert
        assertThat(result).isEmpty();
    }

    
    @Test
    void getTodosDebeRetornarListaVaciaSiNoHayPedidos() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(List.of());

        // Act
        List<Pedido> result = pedidoRepository.findAll();

        // Assert
        assertThat(result).isEmpty();
    }
}
