package com.apps.jpa.repository;

import com.apps.jpa.entity.UserAccountStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountStatusRepositoryJPA extends PagingAndSortingRepository<UserAccountStatus,Integer> {
}
