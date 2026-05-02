package com.tienda.tienda.user.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Integer carritoId;
    private List<Integer> favoritoIds;
}
