package com.tienda.userservice.application.port.output;

public interface GenerateTokenOutputPort {
    String generateToken(String userId);
}