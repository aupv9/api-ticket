package com.apps.authenticate.repository;

import com.apps.authenticate.entity.UserAccountStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface UserAccountStatusRepository {

    int insert(@Param("entity")UserAccountStatus accountStatus);
    int update(@Param("entity")UserAccountStatus accountStatus);
    int deleteById(@Param("id") int id);
    @Cacheable(cacheNames = "userAccountStatus",key = "'UserAccountStatusRepository.findAll'", unless = "#result == null")
    List<UserAccountStatus> findAll(@Param("page") int page, @Param("size") int size);
    @Select(value = "select * from user_account_status where id = #{id}")
    UserAccountStatus findById(int id);


}
