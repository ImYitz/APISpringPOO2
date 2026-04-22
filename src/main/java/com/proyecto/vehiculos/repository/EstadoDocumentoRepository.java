package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.EstadoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoDocumentoRepository extends JpaRepository<EstadoDocumento, Long> {
    Optional<EstadoDocumento> findByNombre(String nombre);
}

