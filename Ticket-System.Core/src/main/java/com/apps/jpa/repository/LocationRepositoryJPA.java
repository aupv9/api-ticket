package com.apps.jpa.repository;

import com.apps.jpa.entity.Location;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepositoryJPA extends PagingAndSortingRepository<Location,Integer> {
}
