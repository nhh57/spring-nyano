package com.sample.domain.repository;

import com.sample.domain.model.TicketDetail;

import java.util.List;
import java.util.Optional;

public interface TicketItemInfrastructureRepository {

     List<TicketDetail> getTicketItems();

    TicketDetail getTicketItem(Long id);
}
