package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.dto.VehiculoDetallePublicoDTO;
import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicoService {

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoPersonaRepository vehiculoPersonaRepository;
    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final PersonaRepository personaRepository;
    private final TipoPersonaRepository tipoPersonaRepository;

    // ─── 1. Vehículos con documentos vencidos ────────────────────────────
    // GET /api/publico/vehiculos/documentos-vencidos

    public List<Vehiculo> vehiculosConDocumentosVencidos() {
        return vehiculoRepository.findByEstadoDocumento("Vencido");
    }

    // ─── 2. Conductores que pueden operar (estado PO) ────────────────────
    // GET /api/publico/conductores/pueden-operar

    public List<VehiculoPersona> conductoresPuedenOperar() {
        return vehiculoPersonaRepository.findConductoresPuedenOperar();
    }

    // ─── 3. Vehículo por placa con conductores y documentos ──────────────
    // GET /api/publico/vehiculos/placa/{placa}

    public VehiculoDetallePublicoDTO vehiculoPorPlacaConDetalle(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa.toUpperCase())
                .orElseThrow(() -> new RuntimeException(
                        "No existe vehículo con placa: " + placa));

        List<VehiculoPersona> conductores =
                vehiculoPersonaRepository.findByVehiculo(vehiculo);

        List<DocumentoVehiculo> documentos =
                documentoVehiculoRepository.findByVehiculo(vehiculo);

        return new VehiculoDetallePublicoDTO(vehiculo, conductores, documentos);
    }

    // ─── 4. Vehículos con documentos por vencer en N días ────────────────
    // GET /api/publico/vehiculos/documentos-por-vencer?dias=30

    public List<Vehiculo> vehiculosConDocumentosPorVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return documentoVehiculoRepository
                .findVehiculosConDocumentosPorVencer(hoy, limite);
    }

    // ─── 5. Total de personas agrupadas por tipo ──────────────────────────
    // GET /api/publico/personas/resumen-por-tipo

    public List<Map<String, Object>> resumenPersonasPorTipo() {
        return tipoPersonaRepository.findAll().stream()
                .map(tipo -> {
                    long total = personaRepository.findAll().stream()
                            .filter(p -> p.getTipoPersona().getId().equals(tipo.getId()))
                            .count();
                    return Map.<String, Object>of(
                            "tipo", tipo.getNombre(),
                            "descripcion", tipo.getNombre().equalsIgnoreCase("A")
                                    ? "Administrador" : "Conductor",
                            "total", total
                    );
                })
                .toList();
    }
}