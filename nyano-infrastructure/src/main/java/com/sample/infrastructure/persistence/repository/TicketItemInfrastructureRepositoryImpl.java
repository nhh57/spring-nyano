package com.sample.infrastructure.persistence.repository;

import com.sample.domain.model.TicketDetail;
import com.sample.domain.repository.TicketItemInfrastructureRepository;
import com.sample.infrastructure.persistence.mapper.jpa.TicketItemInfrastructureJPA;
import com.sample.infrastructure.persistence.mapper.mapping.TicketItemMapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketItemInfrastructureRepositoryImpl implements TicketItemInfrastructureRepository {
    @Autowired
    private TicketItemInfrastructureJPA ticketItemJPA;

    @Autowired
    private TicketItemMapping ticketItemMapping;

    @Transactional
    public List<TicketDetail> getTicketItems() {
        return ticketItemJPA
                .findAll()
                .stream()
                .map(ticketItemMapping::toTicketDetail)
                .collect(Collectors.toList());
    }


    public TicketDetail getTicketItem(Long id) {
        return ticketItemJPA
                .findById(id).map(ticketItemMapping::toTicketDetail).orElse(null);
    }
}
