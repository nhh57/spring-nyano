package com.sample.domain.service.impl;

import com.sample.domain.model.HiDomainEntity;
import com.sample.domain.repository.HiDomainRepository;
import com.sample.domain.service.HiDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiDomainServiceImpl implements HiDomainService {

    @Autowired
    private HiDomainRepository hiDomainRepository;

    @Override
    public String sayHi(String name) {
        return hiDomainRepository.sayHi(name);
    }

    public List<HiDomainEntity> getAllHiEntities(){
        return hiDomainRepository.getAllHiEntities();
    }
}
