package com.backend.OngConciencia.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    //Dependenciaas
    @Autowired
    SecurityFilter securityFilter;

    /*
    * Configuração das origens, requisições http e endpoints
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                //Configura as origens das requisições permitidas e os métodos http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*")); // Permite requisições de qualquer origem(Obs: Posso mudar para um endereço específico)
                    config.setAllowedHeaders(List.of("*")); // Permitir todos os cabeçalhos
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Métodos permitidos
                    return config;
                }))
                //Configura os endpoints
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Configurações para o RestController /ong
                        .requestMatchers(HttpMethod.POST, "/ong").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/ong").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ong").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ong/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ong/search").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/ong/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/ong").hasRole("ADMIN")

                        // Configurações para o RestController /usuario
                        .requestMatchers(HttpMethod.GET, "/user/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/foto").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/user/senha").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/user/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/user/**").permitAll()

                        // Requisições de Entrar e Cadastrar
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                // .oauth2Login(Customizer.withDefaults())

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /*
    * Regenciador de autenticações
    * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*
    * Função para retorna um objeto para encryptografar senhas
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
