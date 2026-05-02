package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request.AddProductToCarritoRequest;
import com.tienda.tienda.product.domain.model.Size;
import org.springframework.stereotype.Component;

@Component
public class AddProductToCarritoRequestMapper {

    public int toProductId(AddProductToCarritoRequest request){
        return request.getProductId();
    }

    public int toQuantity(AddProductToCarritoRequest request){
        return request.getQuantity();
    }

    public Size toSize(AddProductToCarritoRequest request){ return request.getSize(); }
}
