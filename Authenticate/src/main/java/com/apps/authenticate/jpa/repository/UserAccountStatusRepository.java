package com.apps.authenticate.jpa.repository;

import com.apps.authenticate.jpa.entity.UserAccountStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository(value = "userAccountStatus")
public interface UserAccountStatusRepository extends PagingAndSortingRepository<UserAccountStatus,Integer> {
}
