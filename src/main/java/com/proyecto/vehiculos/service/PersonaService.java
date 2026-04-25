package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.config.DataInitializer;
import com.proyecto.vehiculos.dto.PersonaRequestDTO;
import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final TipoPersonaRepository tipoPersonaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── LISTAR ──────────────────────────────────────────────────────────

    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    // ─── OBTENER POR ID ───────────────────────────────────────────────────

    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id));
    }

    // ─── CREAR ────────────────────────────────────────────────────────────

    @Transactional
    public Persona crear(PersonaRequestDTO dto) {

        // 1. Resolver llaves foráneas
        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
                .orElseThrow(() -> new RuntimeException(
                        "Tipo de documento no encontrado: " + dto.getTipoDocumentoId()));

        TipoPersona tipoPersona = tipoPersonaRepository.findById(dto.getTipoPersonaId())
                .orElseThrow(() -> new RuntimeException(
                        "Tipo de persona no encontrado: " + dto.getTipoPersonaId()));

        // 2. Validar que el documento no esté ya registrado
        if (personaRepository.findByDocumento(dto.getDocumento()).isPresent()) {
            throw new RuntimeException(
                    "Ya existe una persona con el documento: " + dto.getDocumento());
        }

        // 3. Construir y guardar la persona
        Persona persona = new Persona();
        persona.setDocumento(dto.getDocumento());
        persona.setTipoDocumento(tipoDoc);
        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setCorreo(dto.getCorreo());
        persona.setTipoPersona(tipoPersona);
        persona = personaRepository.save(persona);

        // 4. Si es ADMINISTRADOR → crear usuario automáticamente
        if (tipoPersona.getNombre().equalsIgnoreCase("A")) {
            crearUsuarioParaAdmin(persona);
        }

        return persona;
    }

    // ─── ACTUALIZAR ───────────────────────────────────────────────────────

    @Transactional
    public Persona actualizar(Long id, PersonaRequestDTO dto) {

        Persona persona = obtenerPorId(id);

        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
                .orElseThrow(() -> new RuntimeException(
                        "Tipo de documento no encontrado: " + dto.getTipoDocumentoId()));

        TipoPersona tipoPersona = tipoPersonaRepository.findById(dto.getTipoPersonaId())
                .orElseThrow(() -> new RuntimeException(
                        "Tipo de persona no encontrado: " + dto.getTipoPersonaId()));

        persona.setDocumento(dto.getDocumento());
        persona.setTipoDocumento(tipoDoc);
        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setCorreo(dto.getCorreo());
        persona.setTipoPersona(tipoPersona);

        return personaRepository.save(persona);
    }

    // ─── HELPER: crear usuario para administrador ─────────────────────────

    private void crearUsuarioParaAdmin(Persona persona) {

        // Login por nemotecnia: PrimeraLetraNombre + PrimeraLetraApellido + Documento
        String login = DataInitializer.generarLogin(persona);

        // Verificar que el login generado no esté ya tomado
        if (usuarioRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException(
                    "Ya existe un usuario con el login: " + login);
        }

        // Password aleatorio (primeros 12 caracteres de un UUID sin guiones)
        String passwordPlano = UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        // APIKey única
        String apikey = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setIdPersona(persona);
        usuario.setPassword(passwordEncoder.encode(passwordPlano));
        usuario.setApikey(apikey);
        usuarioRepository.save(usuario);

        // Imprimir credenciales en consola (el admin debe anotarlas)
        log.info("==============================================================");
        log.info("  NUEVO USUARIO ADMINISTRADOR CREADO");
        log.info("  Login   : {}", login);
        log.info("  Password: {}", passwordPlano);
        log.info("  APIKey  : {}", apikey);
        log.info("==============================================================");
    }
}