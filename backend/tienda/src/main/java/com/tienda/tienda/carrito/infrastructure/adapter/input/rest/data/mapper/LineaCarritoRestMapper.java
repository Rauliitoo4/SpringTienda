package com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import com.tienda.tienda.product.infrastructure.adapter.input.rest.data.mapper.ProductRestMapper;
import org.springframework.stereotype.Component;

@Component
public class LineaCarritoRestMapper {

    private final ProductRestMapper productRestMapper;

    public LineaCarritoRestMapper(ProductRestMapper productRestMapper) {
        this.productRestMapper = productRestMapper;
    }

    public LineaCarritoResponse toResponse(LineaCarrito linea) {
        LineaCarritoResponse response = new LineaCarritoResponse();
        response.setId(linea.getId());
        response.setSubtotal(linea.getSubtotal());
        response.setQuantity(linea.getQuantity());
        response.setCarritoId(linea.getCarritoId());
        response.setProductId(linea.getProductId());

        if (linea.getProduct() != null) {
            response.setProduct(productRestMapper.toResponse(linea.getProduct()));
        }

        return response;
    }
}