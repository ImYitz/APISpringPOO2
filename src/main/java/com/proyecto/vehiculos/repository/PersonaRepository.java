package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDocumento(String documento);

    @Query("SELECT p FROM Persona p WHERE p.tipoPersona.nombre = 'C' " +
            "AND p.fechaVigenciaLicencia IS NOT NULL " +
            "AND p.fechaVigenciaLicencia < :hoy")
    List<Persona> findConductoresConLicenciaVencida(@Param("hoy") LocalDate hoy);
}
