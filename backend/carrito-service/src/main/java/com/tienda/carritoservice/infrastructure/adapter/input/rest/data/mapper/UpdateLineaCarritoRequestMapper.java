package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request.UpdateLineaCarritoRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateLineaCarritoRequestMapper {

    public int toQuantity(UpdateLineaCarritoRequest request){
        return request.getQuantity();
    }
}
