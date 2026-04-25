package com.proyecto.vehiculos.dto;

import lombok.Data;

@Data
public class CambiarEstadoVehiculoPersonaDTO {
    private Long estadoId; // 1=PO (Puede Operar), 2=EA (Espera Aprobación), 3=RO (Restringido)
}