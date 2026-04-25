package com.proyecto.vehiculos.controller;

import com.proyecto.vehiculos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // PUT /api/usuarios/{login}/password
    // Body: { "password": "nuevaContrasena" }
    @PutMapping("/{login}/password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable String login,
            @RequestBody Map<String, String> body) {

        String nuevaPassword = body.get("password");

        if (nuevaPassword == null || nuevaPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'password' es obligatorio"));
        }

        usuarioService.cambiarPassword(login, nuevaPassword);
        return ResponseEntity.ok(Map.of("mensaje", "Password actualizado correctamente"));
    }

    // GET /api/usuarios/{login}/regenerar-apikey
    @GetMapping("/{login}/regenerar-apikey")
    public ResponseEntity<?> regenerarApikey(@PathVariable String login) {
        String nuevaApikey = usuarioService.regenerarApikey(login);
        return ResponseEntity.ok(Map.of(
                "login", login,
                "apikey", nuevaApikey
        ));
    }
}