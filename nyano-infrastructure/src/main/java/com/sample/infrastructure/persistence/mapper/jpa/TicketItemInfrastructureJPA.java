package com.sample.infrastructure.persistence.mapper.jpa;

import com.sample.infrastructure.persistence.model.TicketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketItemInfrastructureJPA extends JpaRepository<TicketItem, Long> {
}
