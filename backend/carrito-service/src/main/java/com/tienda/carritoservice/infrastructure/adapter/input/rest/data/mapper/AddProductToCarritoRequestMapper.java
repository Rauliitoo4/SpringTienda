package com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.request.AddProductToCarritoRequest;
import com.tienda.carritoservice.domain.model.Size;
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
