package com.sample.infrastructure.persistence.mapper.mapping;

import com.sample.domain.model.TicketDetail;
import com.sample.infrastructure.persistence.model.TicketItem;
import org.springframework.stereotype.Component;

@Component
public class TicketItemMapping {
    public TicketDetail toTicketDetail(TicketItem x) {
        return TicketDetail.builder()
                .id(x.getId())
                .name(x.getName())
                .description(x.getDescription())
                .stockInitial(x.getStockInitial())
                .stockAvailable(x.getStockAvailable())
                .isStockPrepared(x.getIsStockPrepared())
                .priceOriginal(x.getPriceOriginal())
                .priceFlash(x.getPriceFlash())
                .saleStartTime(x.getSaleStartTime())
                .saleEndTime(x.getSaleEndTime())
                .status(x.getStatus())
                .activityId(x.getActivityId())
                .updatedAt(x.getUpdatedAt())
                .createdAt(x.getCreatedAt())
                .build();
    }
}
