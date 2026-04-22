package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;

    // --- CRUD ---

    public List<Documento> listar() {
        return documentoRepository.findAll();
    }

    public Documento obtenerPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con id: " + id));
    }

    public Documento crear(Documento documento) {
        return documentoRepository.save(documento);
    }

    public Documento actualizar(Long id, Documento datos) {
        Documento existente = obtenerPorId(id);
        existente.setCodigoDocumento(datos.getCodigoDocumento());
        existente.setNombre(datos.getNombre());
        existente.setAplicaA(datos.getAplicaA());
        existente.setObligatorio(datos.getObligatorio());
        existente.setDescripcion(datos.getDescripcion());
        return documentoRepository.save(existente);
    }

    public void eliminar(Long id) {
        documentoRepository.deleteById(id);
    }

}

