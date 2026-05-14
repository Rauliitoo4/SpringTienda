package com.tienda.productservice.infrastructure.security;

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
                        .pathMatchers(HttpMethod.GET,    "/productos").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/productos/{id}").permitAll()
                        .pathMatchers(HttpMethod.POST,   "/productos").authenticated()
                        .pathMatchers(HttpMethod.PUT,    "/productos/{id}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/productos/{id}").authenticated()
                        .pathMatchers(HttpMethod.POST,   "/productos/{productoId}/promociones/{promocionId}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/productos/{productoId}/promociones/{promocionId}").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                .build();
    }
}