package com.tienda.userservice.application.usecase;

import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginUseCase implements LoginInputPort {

    private final GetUserOutputPort getUserOutputPort;

    public LoginUseCase(GetUserOutputPort getUserOutputPort) {
        this.getUserOutputPort = getUserOutputPort;
    }

    public Mono<User> execute(String email, String password) {
        return getUserOutputPort.findByEmailAndPassword(email, password);
    }
}