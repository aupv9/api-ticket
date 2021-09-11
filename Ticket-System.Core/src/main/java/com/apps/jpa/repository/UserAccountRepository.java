package com.apps.jpa.repository;

import com.apps.domain.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAccountRepository  {
    @Select("Select * from user_account")
    Iterable<UserAccount> findAll();
}
