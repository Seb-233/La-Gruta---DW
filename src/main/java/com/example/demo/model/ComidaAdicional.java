package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comida_adicional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Patrón Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ComidaAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "comida_id", nullable = false)
    private Long comidaId;

    @Column(name = "adicional_id", nullable = false)
    private Long adicionalId;
}
