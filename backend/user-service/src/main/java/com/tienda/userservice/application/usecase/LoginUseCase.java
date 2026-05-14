package com.tienda.userservice.application.usecase;

import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.port.output.GenerateTokenOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginUseCase implements LoginInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final GenerateTokenOutputPort generateTokenOutputPort;

    public LoginUseCase(GetUserOutputPort getUserOutputPort, GenerateTokenOutputPort generateTokenOutputPort) {
        this.getUserOutputPort = getUserOutputPort;
        this.generateTokenOutputPort = generateTokenOutputPort;
    }

    public Mono<String> execute(String email, String password) {
        return getUserOutputPort.findByEmailAndPassword(email, password)
                .map(user -> generateTokenOutputPort.generateToken(String.valueOf(user.getId())));
    }
}