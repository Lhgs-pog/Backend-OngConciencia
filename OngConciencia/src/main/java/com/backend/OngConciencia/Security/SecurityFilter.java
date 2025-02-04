package com.backend.OngConciencia.Security;

import com.backend.OngConciencia.Repository.UsuarioRepository;
import com.backend.OngConciencia.Services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    /*
    * Filtro de requisições http
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoveryToken(request);//Recupera o token
        if(token != null){
            var email = tokenService.validateToken(token);//Recupera o emial do usuário
            UserDetails usuario = usuarioRepository.findByEmail(email);//Recupera dados do usuário

            //Recupera dados e autoridade do usuário
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            //Salva o contexto de segurança do spring para que durante toda a requisição do endpoint reconhça o usuário estando autenticado
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Permite que a requisição continue seu procesamento pelos demais filtros
        filterChain.doFilter(request, response);
    }

    /*
    * Recupera o token do usuário
    * */
    private String recoveryToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}