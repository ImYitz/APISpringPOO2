package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.Trayecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {

    // Trayectos de una ruta ordenados por parada
    List<Trayecto> findByCodigoRutaOrderByOrdenParadaAsc(String codigoRuta);

    // Rutas distintas de un conductor (por documento de persona)
    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t WHERE t.persona.documento = :documento")
    List<String> findCodigosRutaByPersonaDocumento(@Param("documento") String documento);

    // Trayectos de una ruta con info del vehículo (para búsqueda por placa)
    @Query("SELECT t FROM Trayecto t WHERE t.vehiculo.placa = :placa ORDER BY t.codigoRuta, t.ordenParada ASC")
    List<Trayecto> findByVehiculoPlaca(@Param("placa") String placa);

    // Trayectos sin lat/lng (para la tarea programada de geocodificación)
    @Query("SELECT t FROM Trayecto t WHERE t.latitud IS NULL OR t.longitud IS NULL")
    List<Trayecto> findSinCoordenadas();

    // Trayectos donde el vehículo NO está habilitado o el conductor está RO
    @Query("""
        SELECT DISTINCT t FROM Trayecto t
        JOIN DocumentoVehiculo dv ON dv.vehiculo.id = t.vehiculo.id
        JOIN VehiculoPersona vp ON vp.vehiculo.id = t.vehiculo.id
            AND vp.persona.id = t.persona.id
        WHERE dv.estado.nombre = 'Vencido'
           OR vp.estado.nombre = 'RO'
    """)
    List<Trayecto> findRutasNoHabilitadas();
}