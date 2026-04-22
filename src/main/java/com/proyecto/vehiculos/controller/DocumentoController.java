package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.entity.Documento;
import com.proyecto.vehiculos.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    // GET /api/documentos
    @GetMapping
    public ResponseEntity<List<Documento>> listar() {
        return ResponseEntity.ok(documentoService.listar());
    }

    // GET /api/documentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Documento> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(documentoService.obtenerPorId(id));
    }

    // POST /api/documentos
    @PostMapping
    public ResponseEntity<Documento> crear(@RequestBody Documento documento) {
        return ResponseEntity.ok(documentoService.crear(documento));
    }

    // PUT /api/documentos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Documento> actualizar(
            @PathVariable Long id,
            @RequestBody Documento documento) {
        return ResponseEntity.ok(documentoService.actualizar(id, documento));
    }

    // DELETE /api/documentos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        documentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

