package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.entity.Trayecto;
import com.proyecto.vehiculos.repository.TrayectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final TrayectoRepository trayectoRepository;

    // ─── 1. Trayectos de una ruta ordenados por parada ────────────────────
    // GET /api/rutas/{codigoRuta}
    public List<Trayecto> obtenerTrayectosPorRuta(String codigoRuta) {
        List<Trayecto> trayectos =
                trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(codigoRuta);

        if (trayectos.isEmpty()) {
            throw new RuntimeException("No se encontraron trayectos para la ruta: " + codigoRuta);
        }
        return trayectos;
    }

    // ─── 2. Rutas agrupadas de un conductor por su documento ─────────────
    // GET /api/rutas/conductor/{documento}
    public Map<String, Object> obtenerRutasPorConductor(String documento) {
        List<String> codigos =
                trayectoRepository.findCodigosRutaByPersonaDocumento(documento);

        if (codigos.isEmpty()) {
            throw new RuntimeException(
                    "No se encontraron rutas para el conductor con documento: " + documento);
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("documento", documento);
        respuesta.put("totalRutas", codigos.size());
        respuesta.put("codigosRuta", codigos);
        return respuesta;
    }

    // ─── 3. Rutas y conductor agrupados por placa del vehículo ───────────
    // GET /api/rutas/vehiculo/{placa}
    public List<Map<String, Object>> obtenerRutasPorVehiculo(String placa) {
        List<Trayecto> trayectos =
                trayectoRepository.findByVehiculoPlaca(placa.toUpperCase());

        if (trayectos.isEmpty()) {
            throw new RuntimeException(
                    "No se encontraron rutas para el vehículo con placa: " + placa);
        }

        // Agrupar por codigoRuta → conductor único por ruta
        Map<String, Trayecto> porRuta = new LinkedHashMap<>();
        for (Trayecto t : trayectos) {
            porRuta.putIfAbsent(t.getCodigoRuta(), t);
        }

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<String, Trayecto> entry : porRuta.entrySet()) {
            Trayecto t = entry.getValue();
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("codigoRuta", t.getCodigoRuta());
            item.put("placa", t.getVehiculo().getPlaca());
            item.put("conductor", Map.of(
                    "documento", t.getPersona().getDocumento(),
                    "nombres",   t.getPersona().getNombres(),
                    "apellidos", t.getPersona().getApellidos()
            ));
            resultado.add(item);
        }
        return resultado;
    }

    // ─── 4. Rutas donde vehículo NO habilitado o conductor RO ────────────
    // GET /api/rutas/no-habilitadas
    public List<Trayecto> obtenerRutasNoHabilitadas() {
        return trayectoRepository.findRutasNoHabilitadas();
    }
}
