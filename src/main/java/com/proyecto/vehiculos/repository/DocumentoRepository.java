package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    // Buscar documentos por código
    List<Documento> findByCodigoDocumento(String codigoDocumento);
}

