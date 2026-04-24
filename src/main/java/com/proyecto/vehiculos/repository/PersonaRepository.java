package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByDocumento(String documento);
}
