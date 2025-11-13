package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.Pedido;
import com.example.demo.repository.PedidoRepository;

public class PedidoMockTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private DummyPedidoService pedidoService; 

    public PedidoMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    
    static class DummyPedidoService {
        private final PedidoRepository repo;

        DummyPedidoService(PedidoRepository repo) {
            this.repo = repo;
        }

        public List<Pedido> listarActivos() { return repo.findAllActive(); }
        public Optional<Pedido> buscarPorId(Long id) { return repo.findById(id); }
        public Pedido guardar(Pedido p) { return repo.save(p); }
        public boolean eliminar(Long id) {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        }
    }

    // 1. Listar pedidos activos
    @Test
    void listarActivosDebeRetornarPedidos() {
        // Arrange
        Pedido pedido = new Pedido();
        when(pedidoRepository.findAllActive()).thenReturn(List.of(pedido));

        // Act
        List<Pedido> result = pedidoService.listarActivos();

        // Assert
        assertThat(result).hasSize(1);
        verify(pedidoRepository).findAllActive();
    }

    // 2. Buscar pedido por ID
    @Test
    void buscarPorIdDebeRetornarPedido() {
        // Arrange
        Pedido p = new Pedido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(p));

        // Act
        Optional<Pedido> result = pedidoService.buscarPorId(1L);

        // Assert
        assertThat(result).isPresent();
        verify(pedidoRepository).findById(1L);
    }

    //  3. Guardar pedido
    @Test
    void guardarDebeInvocarSave() {
        // Arrange
        Pedido pedido = new Pedido();
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        // Act
        Pedido result = pedidoService.guardar(pedido);

        // Assert
        assertThat(result).isNotNull();
        verify(pedidoRepository).save(pedido);
    }

    //  4. Eliminar pedido existente
    @Test
    void eliminarDebeRetornarTrueSiExiste() {
        // Arrange
        when(pedidoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = pedidoService.eliminar(1L);

        // Assert
        assertThat(result).isTrue();
        verify(pedidoRepository).deleteById(1L);
    }

    //  5. Eliminar pedido inexistente
    @Test
    void eliminarDebeRetornarFalseSiNoExiste() {
        // Arrange
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = pedidoService.eliminar(99L);

        // Assert
        assertThat(result).isFalse();
        verify(pedidoRepository, never()).deleteById(anyLong());
    }
}
