package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToCarritoRequest {
    private int productId;
    private int quantity;
}
