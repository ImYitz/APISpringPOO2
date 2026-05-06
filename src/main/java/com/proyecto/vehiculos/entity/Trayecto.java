package com.proyecto.vehiculos.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "trayecto")
@Data @NoArgsConstructor @AllArgsConstructor
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "codigo_ruta", nullable = false, length = 45)
    private String codigoRuta;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "orden_parada", nullable = false)
    private Integer ordenParada;

    @Column(name = "latitud", precision = 10, scale = 7)
    private java.math.BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private java.math.BigDecimal longitud;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "login", nullable = false)
    private Usuario login;
}