package com.tienda.productservice.application.port.output;

import com.tienda.productservice.application.model.PromotionModel;
import reactor.core.publisher.Flux;
import java.util.List;

public interface GetPromotionOutputPort {
    Flux<PromotionModel> findAllByIds(List<Integer> ids);
}