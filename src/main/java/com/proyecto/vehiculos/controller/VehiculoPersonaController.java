package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.CambiarEstadoVehiculoPersonaDTO;
import com.proyecto.vehiculos.dto.VehiculoPersonaRequestDTO;
import com.proyecto.vehiculos.entity.VehiculoPersona;
import com.proyecto.vehiculos.service.VehiculoPersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculo-persona")
@RequiredArgsConstructor
public class VehiculoPersonaController {

    private final VehiculoPersonaService vehiculoPersonaService;

    // POST /api/vehiculo-persona
    // Asocia un vehículo a un conductor, estado inicial = EA
    @PostMapping
    public ResponseEntity<VehiculoPersona> asociar(
            @RequestBody VehiculoPersonaRequestDTO dto) {
        return ResponseEntity.ok(vehiculoPersonaService.asociar(dto));
    }

    // PUT /api/vehiculo-persona/{id}/estado
    // Cambia el estado: PO (Puede Operar), EA (Espera Aprobación), RO (Restringido)
    @PutMapping("/{id}/estado")
    public ResponseEntity<VehiculoPersona> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoVehiculoPersonaDTO dto) {
        return ResponseEntity.ok(vehiculoPersonaService.cambiarEstado(id, dto));
    }

    // GET /api/vehiculo-persona
    // Lista todas las asociaciones
    @GetMapping
    public ResponseEntity<List<VehiculoPersona>> listar() {
        return ResponseEntity.ok(vehiculoPersonaService.listar());
    }

    // GET /api/vehiculo-persona/vehiculo/{vehiculoId}
    // Lista asociaciones de un vehículo específico
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<VehiculoPersona>> listarPorVehiculo(
            @PathVariable Long vehiculoId) {
        return ResponseEntity.ok(vehiculoPersonaService.listarPorVehiculo(vehiculoId));
    }

    // GET /api/vehiculo-persona/persona/{personaId}
    // Lista asociaciones de una persona específica
    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<VehiculoPersona>> listarPorPersona(
            @PathVariable Long personaId) {
        return ResponseEntity.ok(vehiculoPersonaService.listarPorPersona(personaId));
    }
}