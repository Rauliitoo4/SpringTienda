package com.tienda.userservice.application.usecase;

import com.tienda.tienda.user.application.port.input.DeleteUserInputPort;
import com.tienda.tienda.user.application.port.output.DeleteUserOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteUserUseCase implements DeleteUserInputPort {

    private final DeleteUserOutputPort deleteUserOutputPort;

    public DeleteUserUseCase(DeleteUserOutputPort deleteUserOutputPort) {
        this.deleteUserOutputPort = deleteUserOutputPort;
    }

    public Mono<Boolean> execute(int id) {
        return deleteUserOutputPort.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return deleteUserOutputPort.deleteById(id).thenReturn(true);
                });
    }
}