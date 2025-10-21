// En la carpeta .../repository/
package com.example.demo.repository;

import com.example.demo.model.Domiciliario;
import com.example.demo.model.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.estado != 'entregado'")
    List<Pedido> findAllActive();

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente")
    List<Pedido> findByClienteId(Long idCliente);

    boolean existsByDomiciliarioAsignadoAndEstadoIn(Domiciliario domiciliario, List<String> estados);

}