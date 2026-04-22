package com.tienda.tienda.user.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
