package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.GetUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetUserUseCase {

    private final GetUserRepository getUserRepository;

    public GetUserUseCase(GetUserRepository getUserRepository) {
        this.getUserRepository = getUserRepository;
    }

    public Mono<User> execute(int id) {
        return getUserRepository.findById(id);
    }

    public Flux<User> executeAll() {
        return getUserRepository.findAll();
    }
}