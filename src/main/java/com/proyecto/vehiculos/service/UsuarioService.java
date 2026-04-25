package com.proyecto.vehiculos.service;

import com.proyecto.vehiculos.entity.Usuario;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ─── CAMBIAR PASSWORD ─────────────────────────────────────────────────
    // PUT /api/usuarios/{login}/password
    // Body: { "password": "nuevaContrasena" }

    @Transactional
    public void cambiarPassword(String login, String nuevaPassword) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con login: " + login));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    // ─── REGENERAR APIKEY ─────────────────────────────────────────────────
    // GET /api/usuarios/{login}/regenerar-apikey

    @Transactional
    public String regenerarApikey(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con login: " + login));

        String nuevaApikey = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        usuario.setApikey(nuevaApikey);
        usuarioRepository.save(usuario);

        return nuevaApikey;
    }
}