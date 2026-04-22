package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehiculos")
@Data @NoArgsConstructor @AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK → tipos (Automóvil / Motocicleta)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo", nullable = false)
    private Tipos tipo;

    @Column(unique = true, length = 6, nullable = false)
    @Size(min = 6, max = 6, message = "La placa debe tener exactamente 6 caracteres")
    private String placa;

    // FK → servicios (Pu / Pr)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_servicios", nullable = false)
    private Servicios tipoServicios;

    // FK → Combustibles
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_combustible", nullable = false)
    private Combustible tipoCombustible;

    @Column(nullable = false)
    private Integer capacidad;

    // Color en hexadecimal, ej: #FF5733
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$",
            message = "El color debe ser un código hexadecimal válido. Ej: #FF5733")
    private String color;

    private Integer modelo;
    private String marca;
    private String linea;
}

