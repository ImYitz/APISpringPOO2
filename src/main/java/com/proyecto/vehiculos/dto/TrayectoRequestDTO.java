package com.proyecto.vehiculos.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TrayectoRequestDTO {
    private Long personaId;      // ID del conductor
    private Long vehiculoId;     // ID del vehículo
    private String codigoRuta;   // Ej: RUTA-002
    private String ubicacion;    // Dirección/nombre de la parada
    private Integer ordenParada; // 1, 2, 3...
    private BigDecimal latitud;  // Opcional, la tarea la llena sola
    private BigDecimal longitud; // Opcional
    private Long loginId;        // ID del usuario que registra
}