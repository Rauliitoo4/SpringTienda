package com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
