package com.apps.domain.repository;

import com.apps.jpa.entity.Tier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepositoryJPA extends PagingAndSortingRepository<Tier,Integer> {
}
