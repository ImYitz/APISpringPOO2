package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.EstadoVePersona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoVePersonaRepository extends JpaRepository<EstadoVePersona, Long> {
    Optional<EstadoVePersona> findByNombre(String nombre);
}
