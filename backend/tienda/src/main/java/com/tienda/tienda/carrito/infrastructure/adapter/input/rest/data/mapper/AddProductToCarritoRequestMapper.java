package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request.AddProductToCarritoRequest;
import org.springframework.stereotype.Component;

@Component
public class AddProductToCarritoRequestMapper {

    public int toProductId(AddProductToCarritoRequest request){
        return request.getProductId();
    }

    public int toQuantity(AddProductToCarritoRequest request){
        return request.getQuantity();
    }
}
