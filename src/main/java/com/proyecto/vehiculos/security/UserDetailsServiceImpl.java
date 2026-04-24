package com.proyecto.vehiculos.security;

import com.proyecto.vehiculos.entity.Usuario;
import com.proyecto.vehiculos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + login));

        // El rol viene de tipo_persona: A = ADMINISTRADOR, C = CONDUCTOR
        String tipoPersona = usuario.getIdPersona().getTipoPersona().getNombre();
        String rol = tipoPersona.equalsIgnoreCase("A") ? "ROLE_ADMIN" : "ROLE_CONDUCTOR";

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(rol)))
                .build();
    }
}