package com.sample.domain.repository;



import com.sample.domain.model.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {

    Optional<TicketDetail> findById(Long id);
}
