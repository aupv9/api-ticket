package com.apps.authenticate.repository;

import com.apps.authenticate.entity.UserAccountStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserAccountStatusRepository {

    int insert(@Param("entity")UserAccountStatus accountStatus);
    int update(@Param("entity")UserAccountStatus accountStatus);
    int delete(@Param("id") int id);
    List<UserAccountStatus> findAll();
    @Select(value = "select * from user_account_status where id = #{id}")
    UserAccountStatus findById(int id);


}
