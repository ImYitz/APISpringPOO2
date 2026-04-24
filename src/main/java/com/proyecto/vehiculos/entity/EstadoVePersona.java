package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estado_ve_persona")
@Data @NoArgsConstructor @AllArgsConstructor
public class EstadoVePersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String nombre;  // PO (Puede Operar), EA (Espera de Aprobacion), RO (Restringido para Operar)
}