package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLineaCarritoRequest {
    private int quantity;
}
