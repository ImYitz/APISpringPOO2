package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.VehiculoDetallePublicoDTO;
import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.service.PublicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoController {

    private final PublicoService publicoService;

    // GET /api/publico/vehiculos/documentos-vencidos
    // Lista todos los vehículos que tienen al menos un documento vencido
    @GetMapping("/vehiculos/documentos-vencidos")
    public ResponseEntity<List<Vehiculo>> vehiculosConDocumentosVencidos() {
        return ResponseEntity.ok(publicoService.vehiculosConDocumentosVencidos());
    }

    // GET /api/publico/conductores/pueden-operar
    // Lista conductores con estado PO (Puede Operar)
    @GetMapping("/conductores/pueden-operar")
    public ResponseEntity<List<VehiculoPersona>> conductoresPuedenOperar() {
        return ResponseEntity.ok(publicoService.conductoresPuedenOperar());
    }

    // GET /api/publico/vehiculos/placa/{placa}
    // Retorna el vehículo con sus conductores asociados y sus documentos
    @GetMapping("/vehiculos/placa/{placa}")
    public ResponseEntity<VehiculoDetallePublicoDTO> vehiculoPorPlaca(
            @PathVariable String placa) {
        return ResponseEntity.ok(publicoService.vehiculoPorPlacaConDetalle(placa));
    }

    // GET /api/publico/vehiculos/documentos-por-vencer?dias=30
    // Lista vehículos cuyos documentos vencen dentro de los próximos N días
    @GetMapping("/vehiculos/documentos-por-vencer")
    public ResponseEntity<List<Vehiculo>> vehiculosConDocumentosPorVencer(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(publicoService.vehiculosConDocumentosPorVencer(dias));
    }

    // GET /api/publico/personas/resumen-por-tipo
    // Retorna el total de personas agrupadas por tipo (Administrador / Conductor)
    @GetMapping("/personas/resumen-por-tipo")
    public ResponseEntity<List<Map<String, Object>>> resumenPersonasPorTipo() {
        return ResponseEntity.ok(publicoService.resumenPersonasPorTipo());
    }
}
