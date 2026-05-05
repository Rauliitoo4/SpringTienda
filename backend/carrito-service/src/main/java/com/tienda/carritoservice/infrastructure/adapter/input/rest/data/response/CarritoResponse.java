package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response;

import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CarritoResponse {
    private Integer id;
    private double total;
    private List<LineaCarritoResponse> lineas;
}
