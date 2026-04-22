package com.proyecto.vehiculos.dto;

import lombok.Data;

@Data
public class AgregarDocumentoDTO {

    private Long vehiculoId;   // ID del vehículo
    private Long documentoId;  // ID del documento a agregar
}