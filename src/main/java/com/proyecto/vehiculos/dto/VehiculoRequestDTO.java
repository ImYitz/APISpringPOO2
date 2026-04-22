package com.proyecto.vehiculos.dto;

import lombok.Data;
import java.util.List;

@Data
public class VehiculoRequestDTO {

    private Long tipoId;            // ID de la tabla tipos
    private String placa;            // 6 caracteres exactos
    private Long tipoServiciosId;    // ID de la tabla servicios
    private Long tipoCombustibleId;  // ID de la tabla Combustibles
    private Integer capacidad;
    private String color;            // Código hex: #RRGGBB
    private Integer modelo;
    private String marca;
    private String linea;

    // IDs de documentos a asociar al vehículo
    // Mínimo 1 documento obligatorio
    private List<Long> documentoIds;
}

