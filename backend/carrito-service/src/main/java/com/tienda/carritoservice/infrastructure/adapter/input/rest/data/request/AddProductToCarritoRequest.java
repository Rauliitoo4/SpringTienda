package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.request;

import com.tienda.carritoservice.domain.model.Size;
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
    private Size size;
}
