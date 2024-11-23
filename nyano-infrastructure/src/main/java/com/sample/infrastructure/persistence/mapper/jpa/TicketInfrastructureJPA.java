package com.sample.infrastructure.persistence.mapper.jpa;

import com.sample.infrastructure.persistence.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketInfrastructureJPA extends JpaRepository<Ticket, Long> {
}
