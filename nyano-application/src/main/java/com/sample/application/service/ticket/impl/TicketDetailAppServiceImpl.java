package com.sample.application.service.ticket.impl;

import com.sample.application.service.ticket.TicketDetailAppService;
import com.sample.application.service.ticket.cache.TicketDetailCacheService;
import com.sample.domain.model.TicketDetail;
import com.sample.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        log.info("impl Application : {}", ticketId);
        return ticketDetailCacheService.getTicketDefaultCacheLocal(ticketId, System.currentTimeMillis());
    }
}
