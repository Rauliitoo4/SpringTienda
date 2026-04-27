package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.LineaCarritoEntity;
import org.springframework.stereotype.Component;

@Component
public class LineaCarritoPersistenceMapper {

    public LineaCarritoEntity toEntity(LineaCarrito linea) {
        LineaCarritoEntity entity = new LineaCarritoEntity();
        entity.setId(linea.getId());
        entity.setSubtotal(linea.getSubtotal());
        entity.setQuantity(linea.getQuantity());
        entity.setCarritoId(linea.getCarritoId());
        entity.setProductId(linea.getProductId());
        return entity;
    }

    public LineaCarrito toDomain(LineaCarritoEntity entity) {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(entity.getId());
        linea.setSubtotal(entity.getSubtotal());
        linea.setQuantity(entity.getQuantity());
        linea.setCarritoId(entity.getCarritoId());
        linea.setProductId(entity.getProductId());
        return linea;
    }
}