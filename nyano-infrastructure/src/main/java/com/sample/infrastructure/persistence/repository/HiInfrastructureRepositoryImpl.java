package com.sample.infrastructure.persistence.repository;

import com.sample.domain.model.HiDomainEntity;
import com.sample.domain.repository.HiDomainRepository;
import com.sample.infrastructure.persistence.mapper.jpa.HiInfrastructureJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HiInfrastructureRepositoryImpl implements HiDomainRepository {

    @Autowired
    private HiInfrastructureJPA hiInfrastructureJPA;

    @Override
    public String sayHi(String name) {
        return "Hi Infrastructure::" + name;
    }

    @Override
    public List<HiDomainEntity> getAllHiEntities() {
        return hiInfrastructureJPA.findAll().stream().map(x -> {
            return HiDomainEntity.builder().
                    uuid(x.getUuid())
                    .name(x.getName())
                    .id(x.getId())
                    .build();
        }).collect(Collectors.toList());
    }
}
