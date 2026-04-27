package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.GetUserRepository;
import com.tienda.tienda.user.domain.repository.UpdateUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateUserUseCase {

    private final GetUserRepository getUserRepository;
    private final UpdateUserRepository updateUserRepository;

    public UpdateUserUseCase(GetUserRepository getUserRepository, UpdateUserRepository updateUserRepository) {
        this.getUserRepository = getUserRepository;
        this.updateUserRepository = updateUserRepository;
    }

    public Mono<User> execute(int id, User updatedUser) {
        return getUserRepository.findById(id)
                .flatMap(user -> {
                    if (updatedUser.getName() != null) user.setName(updatedUser.getName());
                    if (updatedUser.getLastname() != null) user.setLastname(updatedUser.getLastname());
                    if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
                    if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
                    if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
                    return updateUserRepository.save(user);
                });
    }
}