package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.dto.AgregarDocumentoDTO;
import com.proyecto.vehiculos.dto.UpdateDocumentoVehiculoDTO;
import com.proyecto.vehiculos.dto.VehiculoRequestDTO;
import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final DocumentoRepository documentoRepository;
    private final DocumentoVehiculoRepository dvRepository;
    private final TiposRepository tiposRepository;
    private final ServiciosRepository serviciosRepository;
    private final CombustibleRepository combustibleRepository;
    private final EstadoDocumentoRepository estadoDocumentoRepository;

    // ─── CRUD BASE ────────────────────────────────────────────

    public List<Vehiculo> listar() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
    }

    // ─── CREAR VEHÍCULO (regla: mínimo 1 doc, estado = En Verificación) ─

    @Transactional
    public Vehiculo crearVehiculo(VehiculoRequestDTO dto) {

        // 1. Validar que venga al menos un documento
        if (dto.getDocumentoIds() == null || dto.getDocumentoIds().isEmpty()) {
            throw new RuntimeException(
                    "No se puede crear un vehículo sin documentos asociados.");
        }

        // 2. Resolver entidades foráneas
        Tipos tipo = tiposRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado: " + dto.getTipoId()));

        Servicios servicio = serviciosRepository.findById(dto.getTipoServiciosId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Combustible combustible = combustibleRepository.findById(dto.getTipoCombustibleId())
                .orElseThrow(() -> new RuntimeException("Combustible no encontrado"));

        // 3. Validar formato de placa según tipo de vehículo
        validarPlaca(dto.getPlaca(), tipo.getTipo());

        // 4. Construir y guardar el vehículo
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setTipo(tipo);
        vehiculo.setPlaca(dto.getPlaca().toUpperCase());
        vehiculo.setTipoServicios(servicio);
        vehiculo.setTipoCombustible(combustible);
        vehiculo.setCapacidad(dto.getCapacidad());
        vehiculo.setColor(dto.getColor());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setLinea(dto.getLinea());
        vehiculo = vehiculoRepository.save(vehiculo);

        // 5. Obtener estado 'En Verificación'
        EstadoDocumento enVerificacion = estadoDocumentoRepository
                .findByNombre("En Verificacion")
                .orElseThrow(() -> new RuntimeException(
                        "Estado 'En Verificacion' no encontrado."));

        // 6. Asociar documentos y cambiar su estado a 'En Verificación'
        for (Long docId : dto.getDocumentoIds()) {
            Documento doc = documentoRepository.findById(docId)
                    .orElseThrow(() -> new RuntimeException("Documento no encontrado: " + docId));

            documentoRepository.save(doc);

            DocumentoVehiculo dv = new DocumentoVehiculo();
            dv.setVehiculo(vehiculo);
            dv.setDocumento(doc);
            dv.setEstado(enVerificacion);
            dvRepository.save(dv);
        }

        return vehiculo;
    }

    // ─── ACTUALIZAR ───────────────────────────────────────────

    @Transactional
    public Vehiculo actualizarVehiculo(Long id, VehiculoRequestDTO dto) {
        Vehiculo vehiculo = obtenerPorId(id);

        Tipos tipo = tiposRepository.findById(dto.getTipoId())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado"));

        validarPlaca(dto.getPlaca(), tipo.getTipo());

        vehiculo.setTipo(tipo);
        vehiculo.setPlaca(dto.getPlaca().toUpperCase());
        vehiculo.setTipoServicios(serviciosRepository.findById(dto.getTipoServiciosId()).orElseThrow());
        vehiculo.setTipoCombustible(combustibleRepository.findById(dto.getTipoCombustibleId()).orElseThrow());
        vehiculo.setCapacidad(dto.getCapacidad());
        vehiculo.setColor(dto.getColor());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setLinea(dto.getLinea());
        return vehiculoRepository.save(vehiculo);
    }

    // ─── ELIMINAR ────────────────────────────────────────────

    @Transactional
    public void eliminarVehiculo(Long id) {
        Vehiculo vehiculo = obtenerPorId(id);
        // Eliminar relaciones primero
        List<DocumentoVehiculo> relaciones = dvRepository.findByVehiculo(vehiculo);
        dvRepository.deleteAll(relaciones);
        vehiculoRepository.delete(vehiculo);
    }

    // ─── AGREGAR DOCUMENTO A VEHÍCULO EXISTENTE ─────────────

    @Transactional
    public DocumentoVehiculo agregarDocumento(AgregarDocumentoDTO dto) {
        Vehiculo vehiculo = obtenerPorId(dto.getVehiculoId());
        Documento documento = documentoRepository.findById(dto.getDocumentoId())
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        if (dvRepository.existsByVehiculoIdAndDocumentoId(dto.getVehiculoId(), dto.getDocumentoId())) {
            throw new RuntimeException("El vehículo ya tiene ese documento asociado.");
        }

        EstadoDocumento enVerificacion = estadoDocumentoRepository
                .findByNombre("En Verificacion")
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        documentoRepository.save(documento);

        DocumentoVehiculo dv = new DocumentoVehiculo();
        dv.setVehiculo(vehiculo);
        dv.setDocumento(documento);
        dv.setEstado(enVerificacion);
        return dvRepository.save(dv);
    }

    // ─── BÚSQUEDAS ESPECIALES ─────────────────────────────────

    public Vehiculo buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa.toUpperCase())
                .orElseThrow(() -> new RuntimeException("No existe vehículo con placa: " + placa));
    }

    public List<Vehiculo> buscarPorTipo(String tipoNombre) {
        return vehiculoRepository.findByTipoNombre(tipoNombre);
    }

    public List<Vehiculo> buscarPorTipoDocumento(String codigoDocumento) {
        return vehiculoRepository.findByTipoDocumento(codigoDocumento);
    }

    public List<Vehiculo> buscarPorEstadoDocumento(String nombreEstado) {
        return vehiculoRepository.findByEstadoDocumento(nombreEstado);
    }

    // ─── VALIDACIÓN DE PLACA ─────────────────────────────────

    private void validarPlaca(String placa, String tipoVehiculo) {
        if (placa == null || placa.length() != 6) {
            throw new RuntimeException("La placa debe tener exactamente 6 caracteres.");
        }
        String p = placa.toUpperCase();
        if (tipoVehiculo.equalsIgnoreCase("Automóvil") ||
                tipoVehiculo.equalsIgnoreCase("Automovil")) {
            // Formato: ABC123 (3 letras + 3 números)
            if (!p.matches("[A-Z]{3}[0-9]{3}")) {
                throw new RuntimeException(
                        "Placa inválida para Automóvil. Formato requerido: ABC123 (3 letras + 3 números)");
            }
        } else if (tipoVehiculo.equalsIgnoreCase("Motocicleta")) {
            // Formato: ABC12D (3 letras + 2 números + 1 letra)
            if (!p.matches("[A-Z]{3}[0-9]{2}[A-Z]{1}")) {
                throw new RuntimeException(
                        "Placa inválida para Motocicleta. Formato requerido: ABC12D (3 letras + 2 números + 1 letra)");
            }
        } else {
            throw new RuntimeException("Tipo de vehículo no reconocido: " + tipoVehiculo);
        }
    }

    @Transactional
    public DocumentoVehiculo actualizarDocumentoVehiculo(Long dvId, UpdateDocumentoVehiculoDTO dto) {
        DocumentoVehiculo dv = dvRepository.findById(dvId)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada con id: " + dvId));

        if (dto.getFechaExpedicion() != null)
            dv.setFechaExpedicion(dto.getFechaExpedicion());

        if (dto.getFechaVencimiento() != null)
            dv.setFechaVencimiento(dto.getFechaVencimiento());

        if (dto.getEstadoId() != null) {
            EstadoDocumento estado = estadoDocumentoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + dto.getEstadoId()));
            dv.setEstado(estado);
        }

        return dvRepository.save(dv);
    }

    public List<DocumentoVehiculo> listarDocumentoVehiculos() {
        return dvRepository.findAll();
    }

    public List<DocumentoVehiculo> listarPorVehiculo(Long vehiculoId) {
        Vehiculo vehiculo = obtenerPorId(vehiculoId);
        return dvRepository.findByVehiculo(vehiculo);
    }

    public List<Vehiculo> buscarPorNombreDocumento(String nombre) {
        return vehiculoRepository.findByNombreDocumento(nombre);
    }
}

