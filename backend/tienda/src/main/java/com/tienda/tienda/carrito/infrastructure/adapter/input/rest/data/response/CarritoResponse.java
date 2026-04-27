package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CarritoResponse {
    private Integer id;
    private double total;
    private List<LineaCarritoResponse> lineas;
}
