package com.sample.domain.service.impl;

import com.sample.domain.model.TicketDetail;
import com.sample.domain.repository.TicketItemInfrastructureRepository;
import com.sample.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    @Autowired
    private TicketItemInfrastructureRepository ticketItemInfrastructureRepository;

    @Override
    public TicketDetail getTicketDetailByID(Long id) {
        return ticketItemInfrastructureRepository.getTicketItem(id);
    }
}
