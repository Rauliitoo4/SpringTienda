package com.tienda.tienda.lineacarrito.application.dto.mapper;
import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class LineaCarritoMapper {

    private final ProductResponseMapper productResponseMapper;

    public LineaCarritoMapper(ProductResponseMapper productResponseMapper){
        this.productResponseMapper = productResponseMapper;
    }

    public LineaCarritoDTO toDTO (LineaCarrito linea) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(linea.getId());
        dto.setQuantity(linea.getQuantity());
        dto.setSubtotal(linea.getSubtotal());
        dto.setCarritoId(linea.getCarritoId());

        if (linea.getProductEntity() != null){
            dto.setProduct(productResponseMapper.toDTO(linea.getProductEntity()));
        }
        return dto;
    }
}
