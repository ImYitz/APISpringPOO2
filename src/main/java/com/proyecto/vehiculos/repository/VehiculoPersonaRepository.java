package com.proyecto.vehiculos.repository;

import com.proyecto.vehiculos.entity.VehiculoPersona;
import com.proyecto.vehiculos.entity.Vehiculo;
import com.proyecto.vehiculos.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface VehiculoPersonaRepository extends JpaRepository<VehiculoPersona, Long> {

    // Todas las asociaciones de un vehículo
    List<VehiculoPersona> findByVehiculo(Vehiculo vehiculo);

    // Todas las asociaciones de una persona
    List<VehiculoPersona> findByPersona(Persona persona);

    // Verificar si ya existe la relación activa
    boolean existsByVehiculoIdAndPersonaId(Long vehiculoId, Long personaId);

    @Query("SELECT vp FROM VehiculoPersona vp WHERE vp.estado.nombre = 'PO'")
    List<VehiculoPersona> findConductoresPuedenOperar();
}