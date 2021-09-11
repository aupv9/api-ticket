package com.apps.domain.repository;

import com.apps.jpa.entity.Theater;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepositoryJPA extends PagingAndSortingRepository<Theater,Integer> {
}
