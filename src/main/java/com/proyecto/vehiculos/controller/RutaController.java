package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.TrayectoRequestDTO;
import com.proyecto.vehiculos.entity.Trayecto;
import com.proyecto.vehiculos.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    // GET /api/rutas/{codigoRuta}
    // Trayectos de una ruta ordenados por parada - requiere token
    @GetMapping("/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> obtenerPorRuta(
            @PathVariable String codigoRuta) {
        return ResponseEntity.ok(rutaService.obtenerTrayectosPorRuta(codigoRuta));
    }

    // GET /api/rutas/conductor/{documento}
    // Rutas agrupadas de un conductor - requiere token
    @GetMapping("/conductor/{documento}")
    public ResponseEntity<Map<String, Object>> obtenerPorConductor(
            @PathVariable String documento) {
        return ResponseEntity.ok(rutaService.obtenerRutasPorConductor(documento));
    }

    // GET /api/rutas/vehiculo/{placa}
    // Rutas y conductor agrupados por vehículo - requiere token
    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<List<Map<String, Object>>> obtenerPorVehiculo(
            @PathVariable String placa) {
        return ResponseEntity.ok(rutaService.obtenerRutasPorVehiculo(placa));
    }

    // GET /api/rutas/no-habilitadas
    // Rutas donde vehículo no está habilitado o conductor está RO - requiere token
    @GetMapping("/no-habilitadas")
    public ResponseEntity<List<Trayecto>> obtenerNoHabilitadas() {
        return ResponseEntity.ok(rutaService.obtenerRutasNoHabilitadas());
    }

    @PostMapping
    public ResponseEntity<Trayecto> crearTrayecto(@RequestBody TrayectoRequestDTO dto) {
        return ResponseEntity.ok(rutaService.crearTrayecto(dto));
    }
}
