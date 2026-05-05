package com.tienda.userservice.application.usecase;

import com.tienda.tienda.user.application.port.input.UpdateUserInputPort;
import com.tienda.tienda.user.application.port.output.GetUserOutputPort;
import com.tienda.tienda.user.application.port.output.UpdateUserOutputPort;
import com.tienda.tienda.user.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateUserUseCase implements UpdateUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final UpdateUserOutputPort updateUserOutputPort;

    public UpdateUserUseCase(GetUserOutputPort getUserOutputPort, UpdateUserOutputPort updateUserOutputPort) {
        this.getUserOutputPort = getUserOutputPort;
        this.updateUserOutputPort = updateUserOutputPort;
    }

    public Mono<User> execute(int id, User updatedUser) {
        return getUserOutputPort.findById(id)
                .flatMap(user -> {
                    if (updatedUser.getName() != null) user.setName(updatedUser.getName());
                    if (updatedUser.getLastname() != null) user.setLastname(updatedUser.getLastname());
                    if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
                    if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
                    if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
                    return updateUserOutputPort.save(user);
                });
    }
}