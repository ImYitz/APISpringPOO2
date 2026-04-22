package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.DocumentoVehiculo;
import com.proyecto.vehiculos.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoVehiculoRepository extends JpaRepository<DocumentoVehiculo, Long> {

    // Obtener todos los documentos de un vehículo específico
    List<DocumentoVehiculo> findByVehiculo(Vehiculo vehiculo);

    // Verificar si ya existe la relación
    boolean existsByVehiculoIdAndDocumentoId(Long vehiculoId, Long documentoId);
}
