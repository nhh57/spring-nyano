package com.sample.application.service.event.impl;

import com.sample.application.service.event.EventAppService;
import com.sample.domain.model.HiDomainEntity;
import com.sample.domain.service.HiDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAppServiceImpl implements EventAppService {
    private static int count = 0;

    // Call Domain Service
    @Resource
    private HiDomainService hiDomainService;

    @Override
    public String SayHi(String who) {
        try {
            Thread.sleep(1000);
            count++;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return hiDomainService.sayHi(who) + "count::" + count + "Hello Application:: " + who;
    }


    public List<HiDomainEntity> getAllHiEntities() {
        return hiDomainService.getAllHiEntities();
    }
}
