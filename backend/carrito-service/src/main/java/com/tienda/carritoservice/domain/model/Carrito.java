package com.tienda.carritoservice.domain.model;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    private Integer id;
    private double total;
    private List<LineaCarrito> lineas;
}
