package com.tienda.carritoservice.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                )
                .authorizeExchange(auth -> auth
                        .pathMatchers(HttpMethod.POST,   "/carritos").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/lineas").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/lineas/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/carritos/{id}").authenticated()
                        .pathMatchers(HttpMethod.POST,   "/carritos/{carritoId}/productos").authenticated()
                        .pathMatchers(HttpMethod.PUT,    "/lineas/{id}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/lineas/{id}").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                .build();
    }
}