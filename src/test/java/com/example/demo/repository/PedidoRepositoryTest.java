package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.Pedido;
import com.example.demo.model.User;
import com.example.demo.model.Role;

@DataJpaTest
public class PedidoRepositoryTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User crearUsuarioConRol(String username) {
        Role rol = Role.builder().name("USER").build();
        roleRepository.save(rol);

        User user = User.builder()
                .username(username)
                .password("1234")
                .build();

        user.getRoles().add(rol);

        return userRepository.save(user);
    }

    // 1. findAllActive
    @Test
    void debeRetornarPedidosActivos() {
        // Arrange
        User user = crearUsuarioConRol("miguel");

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setEstado("EN_PROCESO");
        pedidoRepository.save(pedido);

        // Act
        List<Pedido> activos = pedidoRepository.findAllActive();

        // Assert
        assertThat(activos).isNotEmpty();
        assertThat(activos.get(0).getEstado()).isNotEqualTo("entregado");
    }

    // 2. findByUser
    @Test
    void debeBuscarPedidosPorUsuario() {
        // Arrange
        User user = crearUsuarioConRol("diego");

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setEstado("EN_PROCESO");
        pedidoRepository.save(pedido);

        // Act
        List<Pedido> pedidos = pedidoRepository.findByUser(user);

        // Assert
        assertThat(pedidos).hasSize(1);
    }

    // 3. findByUserAndEstado
    @Test
    void debeBuscarPedidoPorUsuarioYEstado() {
        // Arrange
        User user = crearUsuarioConRol("inyena");

        Pedido pedido = new Pedido();
        pedido.setUser(user);
        pedido.setEstado("CONFIRMADO");
        pedidoRepository.save(pedido);

        // Act
        Optional<Pedido> encontrado =
            pedidoRepository.findByUserAndEstado(user, "CONFIRMADO");

        // Assert
        assertThat(encontrado).isPresent();
    }

    // 4. findByUserOrderByFechaCreacionDesc
    @Test
    void debeRetornarPedidosOrdenadosPorFecha() {
        // Arrange
        User user = crearUsuarioConRol("maria");

        Pedido p1 = new Pedido();
        p1.setUser(user);
        p1.setEstado("EN_PROCESO");
        p1.setFechaCreacion(LocalDateTime.now().minusDays(2));
        pedidoRepository.save(p1);

        Pedido p2 = new Pedido();
        p2.setUser(user);
        p2.setEstado("EN_PROCESO");
        p2.setFechaCreacion(LocalDateTime.now());
        pedidoRepository.save(p2);

        // Act
        List<Pedido> pedidos = pedidoRepository.findByUserOrderByFechaCreacionDesc(user);

        // Assert
        assertThat(pedidos.get(0).getFechaCreacion()).isAfter(pedidos.get(1).getFechaCreacion());
    }

    // 5. existsByDomiciliarioAsignadoAndEstadoIn (sin domiciliarios)
    @Test
    void debeRetornarFalseSiNoHayPedidosConDomiciliario() {
        assertThat(
            pedidoRepository.existsByDomiciliarioAsignadoAndEstadoIn(null, List.of("enviado"))
        ).isFalse();
    }
}
