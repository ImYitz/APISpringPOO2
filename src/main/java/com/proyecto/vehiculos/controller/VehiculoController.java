package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.AgregarDocumentoDTO;
import com.proyecto.vehiculos.dto.UpdateDocumentoVehiculoDTO;
import com.proyecto.vehiculos.dto.UploadPdfDTO;
import com.proyecto.vehiculos.dto.VehiculoRequestDTO;
import com.proyecto.vehiculos.entity.DocumentoVehiculo;
import com.proyecto.vehiculos.entity.Vehiculo;
import com.proyecto.vehiculos.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // GET /api/vehiculos — listar todos
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        return ResponseEntity.ok(vehiculoService.listar());
    }

    // GET /api/vehiculos/{id} — obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    // POST /api/vehiculos — crear vehículo con documentos
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody VehiculoRequestDTO dto) {
        return ResponseEntity.ok(vehiculoService.crearVehiculo(dto));
    }

    // PUT /api/vehiculos/{id} — actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(
            @PathVariable Long id,
            @RequestBody VehiculoRequestDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizarVehiculo(id, dto));
    }

    // DELETE /api/vehiculos/{id} — eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Búsquedas especiales ─────────────────────────────────────

    // GET /api/vehiculos/placa/{placa}
    @GetMapping("/placa/{placa}")
    public ResponseEntity<Vehiculo> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.buscarPorPlaca(placa));
    }

    // GET /api/vehiculos/tipo/{tipo}  (Automóvil o Motocicleta)
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Vehiculo>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(vehiculoService.buscarPorTipo(tipo));
    }

    // GET /api/vehiculos/documento/{codigo}  (código del documento, ej: SOAT)
    @GetMapping("/documento/{codigo}")
    public ResponseEntity<List<Vehiculo>> buscarPorTipoDocumento(
            @PathVariable String codigo) {
        return ResponseEntity.ok(vehiculoService.buscarPorTipoDocumento(codigo));
    }

    // GET /api/vehiculos/estado/{estado}  (Habilitado, Vencido, En Verificacion)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Vehiculo>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(vehiculoService.buscarPorEstadoDocumento(estado));
    }

    // POST /api/vehiculos/agregar-documento
    @PostMapping("/agregar-documento")
    public ResponseEntity<?> agregarDocumento(@RequestBody AgregarDocumentoDTO dto) {
        return ResponseEntity.ok(vehiculoService.agregarDocumento(dto));
    }

    // PUT /api/vehiculos/documento-vehiculo/{id}
    @PutMapping("/documento-vehiculo/{id}")
    public ResponseEntity<DocumentoVehiculo> actualizarDocumentoVehiculo(
            @PathVariable Long id,
            @RequestBody UpdateDocumentoVehiculoDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizarDocumentoVehiculo(id, dto));
    }

    // GET /api/vehiculos/documento-vehiculo
    // Lista todas las relaciones
    @GetMapping("/documento-vehiculo")
    public ResponseEntity<List<DocumentoVehiculo>> listarDocumentoVehiculos() {
        return ResponseEntity.ok(vehiculoService.listarDocumentoVehiculos());
    }

    // GET /api/vehiculos/documento-vehiculo/vehiculo/{vehiculoId}
    // Lista solo las relaciones de un vehículo específico
    @GetMapping("/documento-vehiculo/vehiculo/{vehiculoId}")
    public ResponseEntity<List<DocumentoVehiculo>> listarPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(vehiculoService.listarPorVehiculo(vehiculoId));
    }

    // GET /api/vehiculos/documento/nombre/{nombre}
    @GetMapping("/documento/nombre/{nombre}")
    public ResponseEntity<List<Vehiculo>> buscarPorNombreDocumento(@PathVariable String nombre) {
        return ResponseEntity.ok(vehiculoService.buscarPorNombreDocumento(nombre));
    }

    @PostMapping("/cargar-pdf")
    public ResponseEntity<?> cargarPdf(@RequestBody UploadPdfDTO dto) {
        return ResponseEntity.ok(vehiculoService.cargarPdfs(dto));
    }
}

