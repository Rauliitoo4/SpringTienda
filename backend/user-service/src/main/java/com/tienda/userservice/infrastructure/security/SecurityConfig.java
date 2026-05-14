package com.tienda.userservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Desactivar sesión — JWT es stateless
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // Desactivar CSRF — API REST stateless no lo necesita
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Respuesta 401 cuando no hay token o es inválido
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                )
                // Reglas de autorización
                .authorizeExchange(auth -> auth
                        // Públicos
                        .pathMatchers(HttpMethod.POST,   "/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST,   "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/usuarios/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/usuarios/{id}").permitAll()
                        // Requieren token
                        .pathMatchers(HttpMethod.PUT,    "/usuarios/{id}").authenticated()
                        .pathMatchers(HttpMethod.POST,   "/usuarios/{id}/favoritos/{favId}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/usuarios/{id}/favoritos/{favId}").authenticated()
                        .anyExchange().authenticated()
                )
                // Filtro JWT antes del filtro de autorización de Spring
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}