package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculo_persona")
@Data @NoArgsConstructor @AllArgsConstructor
public class VehiculoPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK → vehiculos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    // FK → persona
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "fecha_asociacion", nullable = false)
    private LocalDateTime fechaAsociacion;

    // FK → estado_ve_persona (PO, EA, RO)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado", nullable = false)
    private EstadoVePersona estado;
}