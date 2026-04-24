package com.proyecto.vehiculos.security;

import com.proyecto.vehiculos.repository.UsuarioRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    private static final List<String> PUBLIC_PREFIXES = List.of(
            "/auth/",
            "/api/publico/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean esPublica = PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
        if (esPublica) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null
                && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal().toString());

        if (autenticado) {
            String apiKey = request.getHeader("X-API-KEY");

            if (apiKey == null || apiKey.isBlank()) {
                escribirError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Falta la cabecera X-API-KEY");
                return;
            }

            if (usuarioRepository.findByApikey(apiKey).isEmpty()) {
                escribirError(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "APIKey invalida");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void escribirError(HttpServletResponse response, int status, String mensaje)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        // JSON manual, sin dependencia de ObjectMapper
        response.getWriter().write("{\"error\":\"" + mensaje + "\"}");
    }
}