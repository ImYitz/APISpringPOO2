package com.proyecto.vehiculos.config;

import com.proyecto.vehiculos.entity.*;
import com.proyecto.vehiculos.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Crea un usuario ADMINISTRADOR inicial si la tabla usuario está vacía.
 * Útil para el primer arranque de la aplicación.
 *
 * Usuario inicial:
 *   login   : admin
 *   password: Admin2026!
 *   apikey  : generado automáticamente (impreso en consola)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final TipoPersonaRepository tipoPersonaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) return; // ya existe al menos un usuario

        log.info("=== No se encontraron usuarios. Creando administrador inicial... ===");

        // Buscar o crear TipoPersona ADMINISTRADOR (nombre = 'A')
        TipoPersona tipoAdmin = tipoPersonaRepository
                .findAll().stream()
                .filter(t -> t.getNombre().equalsIgnoreCase("A"))
                .findFirst()
                .orElseGet(() -> {
                    TipoPersona t = new TipoPersona();
                    t.setNombre("A");
                    return tipoPersonaRepository.save(t);
                });

        // Buscar TipoDocumento CC
        TipoDocumento tipoCC = tipoDocumentoRepository
                .findAll().stream()
                .filter(t -> t.getNombre().equalsIgnoreCase("CC"))
                .findFirst()
                .orElseGet(() -> {
                    TipoDocumento t = new TipoDocumento();
                    t.setNombre("CC");
                    return tipoDocumentoRepository.save(t);
                });

        // Crear Persona administradora
        Persona persona = new Persona();
        persona.setNombres("Admin");
        persona.setApellidos("Sistema");
        persona.setDocumento("00000001");
        persona.setCorreo("admin@sistema.com");
        persona.setTipoDocumento(tipoCC);
        persona.setTipoPersona(tipoAdmin);
        persona = personaRepository.save(persona);

        // Generar APIKey única
        String apikey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String passwordPlano = "Admin2026!";

        // Crear Usuario
        // Login por nemotecnia: PrimeraLetraNombre + PrimeraLetraApellido + Documento
        String login = generarLogin(persona);

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setIdPersona(persona);
        usuario.setPassword(passwordEncoder.encode(passwordPlano));
        usuario.setApikey(apikey);
        usuarioRepository.save(usuario);

        log.info("==============================================================");
        log.info("  USUARIO ADMINISTRADOR INICIAL CREADO");
        log.info("  Login   : {}", login);
        log.info("  Password: {}", passwordPlano);
        log.info("  APIKey  : {}", apikey);
        log.info("==============================================================");
    }

    public static String generarLogin(Persona persona) {
        String primeraLetraNombre = persona.getNombres()
                .trim().substring(0, 1).toUpperCase();
        String primeraLetraApellido = persona.getApellidos()
                .trim().substring(0, 1).toUpperCase();
        return primeraLetraNombre + primeraLetraApellido + persona.getDocumento();
    }
}