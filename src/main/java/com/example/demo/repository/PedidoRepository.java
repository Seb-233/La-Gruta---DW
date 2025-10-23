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

    List<Pedido> findByUser(User user);

    // ✅ Búsqueda insensible a mayúsculas
    @Query("SELECT p FROM Pedido p WHERE p.user = :user AND LOWER(p.estado) = LOWER(:estado)")
    Optional<Pedido> findByUserAndEstado(@Param("user") User user, @Param("estado") String estado);

    boolean existsByDomiciliarioAsignadoAndEstadoIn(Domiciliario domiciliario, List<String> estados);

    List<Pedido> findByUserOrderByFechaCreacionDesc(User user);
}
