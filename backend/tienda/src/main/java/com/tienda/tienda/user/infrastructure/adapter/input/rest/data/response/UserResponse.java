package com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class UserResponse {
    private int id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private int carritoId;
    private List<Integer> favoritoIds;
}
