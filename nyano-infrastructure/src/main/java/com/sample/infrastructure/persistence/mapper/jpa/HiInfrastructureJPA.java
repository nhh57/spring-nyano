package com.sample.infrastructure.persistence.mapper.jpa;

import com.sample.infrastructure.persistence.model.HiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiInfrastructureJPA extends JpaRepository<HiEntity, Long> {
}
