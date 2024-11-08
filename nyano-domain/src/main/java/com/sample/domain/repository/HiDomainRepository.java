package com.sample.domain.repository;

import com.sample.domain.model.HiDomainEntity;

import java.util.List;

public interface HiDomainRepository {
    String sayHi(String name);
    public List<HiDomainEntity> getAllHiEntities();

}
