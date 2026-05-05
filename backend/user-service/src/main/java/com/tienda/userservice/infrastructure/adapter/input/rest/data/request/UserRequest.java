package com.tienda.userservice.infrastructure.adapter.input.rest.data.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserRequest {
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
