package com.proyecto.vehiculos.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateDocumentoVehiculoDTO {
    private LocalDate fechaExpedicion;
    private LocalDate fechaVencimiento;
    private Long estadoId; // 1=Habilitado, 2=Vencido, 3=En Verificacion
}