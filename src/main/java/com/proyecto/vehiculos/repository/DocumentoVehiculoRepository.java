package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.DocumentoVehiculo;
import com.proyecto.vehiculos.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.proyecto.vehiculos.entity.Vehiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface DocumentoVehiculoRepository extends JpaRepository<DocumentoVehiculo, Long> {

    // Obtener todos los documentos de un vehículo específico
    List<DocumentoVehiculo> findByVehiculo(Vehiculo vehiculo);

    // Verificar si ya existe la relación
    boolean existsByVehiculoIdAndDocumentoId(Long vehiculoId, Long documentoId);

    @Query("SELECT DISTINCT dv.vehiculo FROM DocumentoVehiculo dv " +
            "WHERE dv.fechaVencimiento BETWEEN :hoy AND :limite")
    List<Vehiculo> findVehiculosConDocumentosPorVencer(
            @Param("hoy") LocalDate hoy,
            @Param("limite") LocalDate limite);
}
