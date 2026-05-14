package com.tienda.carritoservice.infrastructure.security;

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
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                )
                .authorizeExchange(auth -> auth
                        // Públicos
                        .pathMatchers(HttpMethod.POST,    "/carritos").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/lineas").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/lineas/{id}").permitAll()
                        // Requieren token
                        .pathMatchers(HttpMethod.GET,    "/carritos/{id}").authenticated()
                        .pathMatchers(HttpMethod.POST,   "/carritos/{carritoId}/productos").authenticated()
                        .pathMatchers(HttpMethod.PUT,    "/lineas/{id}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/lineas/{id}").authenticated()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}