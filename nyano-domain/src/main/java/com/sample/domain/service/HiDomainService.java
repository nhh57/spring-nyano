package com.sample.domain.service;

import com.sample.domain.model.HiDomainEntity;

import java.util.List;

public interface HiDomainService {
    String sayHi(String name);

    public List<HiDomainEntity> getAllHiEntities();
}
