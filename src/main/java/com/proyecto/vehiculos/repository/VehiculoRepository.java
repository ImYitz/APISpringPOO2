package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Buscar por placa
    Optional<Vehiculo> findByPlaca(String placa);

    // Buscar por tipo de vehículo (nombre del tipo: Automóvil / Motocicleta)
    @Query("SELECT v FROM Vehiculo v WHERE v.tipo.tipo = :tipoNombre")
    List<Vehiculo> findByTipoNombre(@Param("tipoNombre") String tipoNombre);

    // Buscar vehículos que tienen un documento con cierto código
    @Query("SELECT DISTINCT dv.vehiculo FROM DocumentoVehiculo dv " +
            "WHERE dv.documento.codigoDocumento = :codigoDocumento")
    List<Vehiculo> findByTipoDocumento(@Param("codigoDocumento") String codigoDocumento);

    // Buscar vehículos cuyo documento tiene cierto estado
    @Query("SELECT DISTINCT dv.vehiculo FROM DocumentoVehiculo dv " +
            "WHERE dv.estado.nombre = :nombreEstado")
    List<Vehiculo> findByEstadoDocumento(@Param("nombreEstado") String nombreEstado);

    @Query("SELECT DISTINCT dv.vehiculo FROM DocumentoVehiculo dv " +
            "WHERE dv.documento.nombre = :nombre")
    List<Vehiculo> findByNombreDocumento(@Param("nombre") String nombre);
}
