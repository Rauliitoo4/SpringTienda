package com.tienda.tienda.dto.mapper;
import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.model.LineaCarrito;
import org.springframework.stereotype.Component;

@Component
public class LineaCarritoMapper {

    private final ProductMapper productMapper;

    public LineaCarritoMapper(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public LineaCarritoDTO toDTO (LineaCarrito linea) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(linea.getId());
        dto.setCantidad(linea.getCantidad());
        dto.setSubtotal(linea.getSubtotal());
        dto.setCarritoId(linea.getCarritoId());

        if (linea.getProducto() != null){
            dto.setProducto(productMapper.toDTO(linea.getProducto()));
        }
        return dto;
    }
}
