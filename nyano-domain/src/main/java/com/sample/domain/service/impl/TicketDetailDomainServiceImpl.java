package com.sample.domain.service.impl;

import com.sample.domain.entity.TicketDetail;
import com.sample.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    @Override
    public TicketDetail getTicketDetailByID(Long id) {
        return null;
    }
}
