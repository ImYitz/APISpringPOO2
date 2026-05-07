package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksService {

    private final PersonaRepository personaRepository;
    private final VehiculoPersonaRepository vehiculoPersonaRepository;
    private final EstadoVePersonaRepository estadoVePersonaRepository;
    private final DocumentoVehiculoRepository documentoVehiculoRepository;
    private final EstadoDocumentoRepository estadoDocumentoRepository;
    private final TrayectoRepository trayectoRepository;
    private final RestTemplate restTemplate;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    // ─── TAREA 1: Licencias vencidas → estado RO ──────────────────────────
    // Corre cada 2 minutos
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void verificarLicenciasVencidas() {
        log.info(">>> [TAREA 1] Verificando licencias vencidas...");

        EstadoVePersona estadoRO = estadoVePersonaRepository.findByNombre("RO")
                .orElse(null);
        if (estadoRO == null) {
            log.warn("Estado RO no encontrado en BD");
            return;
        }

        List<Persona> conductores =
                personaRepository.findConductoresConLicenciaVencida(LocalDate.now());

        for (Persona conductor : conductores) {
            List<VehiculoPersona> relaciones =
                    vehiculoPersonaRepository.findByPersona(conductor);

            for (VehiculoPersona vp : relaciones) {
                if (!vp.getEstado().getNombre().equals("RO")) {
                    vp.setEstado(estadoRO);
                    vehiculoPersonaRepository.save(vp);
                    log.info("Conductor {} restringido (licencia vencida el {})",
                            conductor.getDocumento(),
                            conductor.getFechaVigenciaLicencia());
                }
            }
        }
        log.info(">>> [TAREA 1] Finalizada. {} conductores afectados.", conductores.size());
    }

    // ─── TAREA 2: Documentos vencidos → estado VENCIDO ───────────────────
    // Corre cada 2 minutos
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void verificarDocumentosVencidos() {
        log.info(">>> [TAREA 2] Verificando documentos vencidos...");

        EstadoDocumento estadoVencido = estadoDocumentoRepository.findByNombre("Vencido")
                .orElse(null);
        if (estadoVencido == null) {
            log.warn("Estado 'Vencido' no encontrado en BD");
            return;
        }

        List<DocumentoVehiculo> vencidos =
                documentoVehiculoRepository.findDocumentosVencidos(LocalDate.now());

        for (DocumentoVehiculo dv : vencidos) {
            dv.setEstado(estadoVencido);
            documentoVehiculoRepository.save(dv);
            log.info("Documento '{}' del vehículo placa {} marcado como VENCIDO (venció el {})",
                    dv.getDocumento().getNombre(),
                    dv.getVehiculo().getPlaca(),
                    dv.getFechaVencimiento());
        }
        log.info(">>> [TAREA 2] Finalizada. {} documentos marcados como vencidos.", vencidos.size());
    }

    // ─── TAREA 3: Geocodificar trayectos sin coordenadas ─────────────────
    // Corre cada 90 segundos
    @Scheduled(fixedRate = 90000)
    @Transactional
    public void geocodificarTrayectos() {
        log.info(">>> [TAREA 3] Geocodificando trayectos sin coordenadas...");

        List<Trayecto> sinCoordenadas = trayectoRepository.findSinCoordenadas();

        for (Trayecto trayecto : sinCoordenadas) {
            try {
                String url = "https://maps.googleapis.com/maps/api/geocode/json"
                        + "?address=" + trayecto.getUbicacion().replace(" ", "+")
                        + "&key=" + googleMapsApiKey;

                Map<?, ?> respuesta = restTemplate.getForObject(url, Map.class);

                if (respuesta != null && "OK".equals(respuesta.get("status"))) {
                    List<?> results = (List<?>) respuesta.get("results");
                    Map<?, ?> geometry = (Map<?, ?>) ((Map<?, ?>) results.get(0)).get("geometry");
                    Map<?, ?> location = (Map<?, ?>) geometry.get("location");

                    BigDecimal lat = new BigDecimal(location.get("lat").toString());
                    BigDecimal lng = new BigDecimal(location.get("lng").toString());

                    trayecto.setLatitud(lat);
                    trayecto.setLongitud(lng);
                    trayectoRepository.save(trayecto);

                    log.info("Trayecto id={} ubicacion='{}' → lat={}, lng={}",
                            trayecto.getId(), trayecto.getUbicacion(), lat, lng);
                } else {
                    log.warn("Google Maps no encontró resultados para: {}", trayecto.getUbicacion());
                }

            } catch (Exception e) {
                log.error("Error geocodificando trayecto id={}: {}", trayecto.getId(), e.getMessage());
            }
        }
        log.info(">>> [TAREA 3] Finalizada. {} trayectos procesados.", sinCoordenadas.size());
    }
}