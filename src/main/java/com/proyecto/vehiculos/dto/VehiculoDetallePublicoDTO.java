package com.proyecto.vehiculos.dto;

import com.proyecto.vehiculos.entity.DocumentoVehiculo;
import com.proyecto.vehiculos.entity.Vehiculo;
import com.proyecto.vehiculos.entity.VehiculoPersona;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VehiculoDetallePublicoDTO {

    private Vehiculo vehiculo;
    private List<VehiculoPersona> conductores;
    private List<DocumentoVehiculo> documentos;
}
