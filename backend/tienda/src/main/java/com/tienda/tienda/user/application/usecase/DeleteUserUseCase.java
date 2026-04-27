package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.domain.repository.DeleteUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteUserUseCase {

    private final DeleteUserRepository deleteUserRepository;

    public DeleteUserUseCase(DeleteUserRepository deleteUserRepository) {
        this.deleteUserRepository = deleteUserRepository;
    }

    public Mono<Boolean> execute(int id) {
        return deleteUserRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return deleteUserRepository.deleteById(id).thenReturn(true);
                });
    }
}