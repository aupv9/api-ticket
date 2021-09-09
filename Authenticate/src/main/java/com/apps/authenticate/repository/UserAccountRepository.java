package com.apps.authenticate.repository;

import com.apps.authenticate.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAccountRepository  {
    @Select("Select * from user_account")
    Iterable<UserAccount> findAll();
}
