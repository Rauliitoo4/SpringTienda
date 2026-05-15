package com.tienda.userservice.infrastructure.security;

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
                        .pathMatchers(HttpMethod.POST,   "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/usuarios/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET,    "/usuarios").permitAll()
                        .pathMatchers(HttpMethod.GET, "/usuarios/email/{email}").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/usuarios/{id}").permitAll()
                        .pathMatchers(HttpMethod.PUT,    "/usuarios/{id}").authenticated()
                        .pathMatchers(HttpMethod.POST,   "/usuarios/{id}/favoritos/{favId}").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/usuarios/{id}/favoritos/{favId}").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                .build();
    }
}