package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.PersonaRequestDTO;
import com.proyecto.vehiculos.entity.Persona;
import com.proyecto.vehiculos.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    // GET /api/personas
    @GetMapping
    public ResponseEntity<List<Persona>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    // GET /api/personas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    // POST /api/personas
    @PostMapping
    public ResponseEntity<Persona> crear(@RequestBody PersonaRequestDTO dto) {
        return ResponseEntity.ok(personaService.crear(dto));
    }

    // PUT /api/personas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizar(
            @PathVariable Long id,
            @RequestBody PersonaRequestDTO dto) {
        return ResponseEntity.ok(personaService.actualizar(id, dto));
    }
}