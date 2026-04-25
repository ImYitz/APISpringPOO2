package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.dto.CambiarEstadoVehiculoPersonaDTO;
import com.proyecto.vehiculos.dto.VehiculoPersonaRequestDTO;
import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoPersonaService {

    private final VehiculoPersonaRepository vehiculoPersonaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PersonaRepository personaRepository;
    private final EstadoVePersonaRepository estadoVePersonaRepository;

    // ─── ASOCIAR VEHÍCULO A CONDUCTOR ────────────────────────────────────
    // POST /api/vehiculo-persona

    @Transactional
    public VehiculoPersona asociar(VehiculoPersonaRequestDTO dto) {

        // 1. Verificar que el vehículo exista
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new RuntimeException(
                        "Vehículo no encontrado con id: " + dto.getVehiculoId()));

        // 2. Verificar que la persona exista
        Persona persona = personaRepository.findById(dto.getPersonaId())
                .orElseThrow(() -> new RuntimeException(
                        "Persona no encontrada con id: " + dto.getPersonaId()));

        // 3. Verificar que no exista ya esa asociación
        if (vehiculoPersonaRepository.existsByVehiculoIdAndPersonaId(
                dto.getVehiculoId(), dto.getPersonaId())) {
            throw new RuntimeException(
                    "Ya existe una asociación entre el vehículo y la persona indicados.");
        }

        // 4. Estado inicial: EA (Espera de Aprobación)
        EstadoVePersona estadoInicial = estadoVePersonaRepository.findByNombre("EA")
                .orElseThrow(() -> new RuntimeException(
                        "Estado 'EA' no encontrado en la base de datos."));

        // 5. Crear y guardar la relación
        VehiculoPersona vp = new VehiculoPersona();
        vp.setVehiculo(vehiculo);
        vp.setPersona(persona);
        vp.setFechaAsociacion(LocalDateTime.now());
        vp.setEstado(estadoInicial);

        return vehiculoPersonaRepository.save(vp);
    }

    // ─── CAMBIAR ESTADO DE LA RELACIÓN ───────────────────────────────────
    // PUT /api/vehiculo-persona/{id}/estado

    @Transactional
    public VehiculoPersona cambiarEstado(Long id, CambiarEstadoVehiculoPersonaDTO dto) {

        // 1. Buscar la relación
        VehiculoPersona vp = vehiculoPersonaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Relación vehículo-persona no encontrada con id: " + id));

        // 2. Buscar el nuevo estado
        EstadoVePersona nuevoEstado = estadoVePersonaRepository.findById(dto.getEstadoId())
                .orElseThrow(() -> new RuntimeException(
                        "Estado no encontrado con id: " + dto.getEstadoId()));

        // 3. Actualizar y guardar
        vp.setEstado(nuevoEstado);
        return vehiculoPersonaRepository.save(vp);
    }

    // ─── LISTAR TODAS LAS ASOCIACIONES ───────────────────────────────────
    // GET /api/vehiculo-persona

    public List<VehiculoPersona> listar() {
        return vehiculoPersonaRepository.findAll();
    }

    // ─── LISTAR ASOCIACIONES DE UN VEHÍCULO ──────────────────────────────
    // GET /api/vehiculo-persona/vehiculo/{vehiculoId}

    public List<VehiculoPersona> listarPorVehiculo(Long vehiculoId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException(
                        "Vehículo no encontrado con id: " + vehiculoId));
        return vehiculoPersonaRepository.findByVehiculo(vehiculo);
    }

    // ─── LISTAR ASOCIACIONES DE UNA PERSONA ──────────────────────────────
    // GET /api/vehiculo-persona/persona/{personaId}

    public List<VehiculoPersona> listarPorPersona(Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException(
                        "Persona no encontrada con id: " + personaId));
        return vehiculoPersonaRepository.findByPersona(persona);
    }
}
