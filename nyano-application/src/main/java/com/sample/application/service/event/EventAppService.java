package com.sample.application.service.event;

import com.sample.domain.model.HiDomainEntity;

import java.util.List;

public interface EventAppService {
    String SayHi(String who);
    List<HiDomainEntity> getAllHiEntities();
}
