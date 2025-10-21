// En la carpeta .../repository/
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Domiciliario;
import com.example.demo.model.Pedido;
import com.example.demo.model.User;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.estado != 'entregado'")
    List<Pedido> findAllActive();

    // Pedidos por usuario (campo 'user' en la entidad)
    List<Pedido> findByUser(User user);

    // Carrito activo del usuario
    Optional<Pedido> findByUserAndEstado(User user, String estado);

    @Query("SELECT p FROM Pedido p WHERE p.user.id = :userId AND p.estado = :estado")
    Optional<Pedido> findByUserIdAndEstado(@Param("userId") Long userId, @Param("estado") String estado);

    // Validación para no asignar domiciliario con pedidos activos
    boolean existsByDomiciliarioAsignadoAndEstadoIn(Domiciliario domiciliario, List<String> estados);
}
