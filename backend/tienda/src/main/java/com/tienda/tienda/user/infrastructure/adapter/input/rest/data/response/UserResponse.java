package com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response;

import lombok.Data;

@Data
public class UserResponse {
    private int id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private int carritoId;
}
