package com.personalproject.tasklist.security;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.personalproject.tasklist.users.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FilterRequests extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var servletPath = request.getServletPath();
        // Definindo o endpoint onde todas as regras abaixo devem ser aplicadas
        if (servletPath.startsWith("/tasks/")) {
            // Processo para pegar a autenticação (usuário e senha)
            var authorization = request.getHeader("Authorization");
            
            var authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            
            var stringfyAuth = new String(authDecode);
            String[] credentials = stringfyAuth.split(":");
            String username = credentials[0];
            String password = credentials[1];

            // Validar usuário
            var user = this.userRepository.findByUsername(username);
            if (username == null) {
                response.sendError(401);
            } else {
                // Validar senha
                var verifyedPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (verifyedPassword.verified) {
                    request.setAttribute("userId", user.getId());
                    // Seguir com a solicitação do request
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
