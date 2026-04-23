package com.tienda.tienda.user.application.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private int id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private int carritoId;
}
