package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.dto.LoginRequestDTO;
import com.proyecto.vehiculos.dto.LoginResponseDTO;
import com.proyecto.vehiculos.entity.Usuario;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import com.proyecto.vehiculos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    /**
     * POST /auth/login
     * Body: { "login": "JA1110176771", "password": "..." }
     * Devuelve: { "token": "...", "login": "...", "tipoPersona": "A" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(java.util.Map.of("error", "Credenciales inválidas"));
        }

        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(dto.getLogin());
        String tipo = usuario.getIdPersona().getTipoPersona().getNombre();

        return ResponseEntity.ok(new LoginResponseDTO(token, dto.getLogin(), tipo));
    }
}