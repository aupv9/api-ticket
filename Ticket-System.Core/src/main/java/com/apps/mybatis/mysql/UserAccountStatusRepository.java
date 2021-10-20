package com.apps.mybatis.mysql;

import com.apps.domain.entity.UserAccountStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface UserAccountStatusRepository {

    int insert(@Param("entity")UserAccountStatus accountStatus);
    int update(@Param("entity")UserAccountStatus accountStatus);
    int deleteById(@Param("id") int id);

    @Select("SELECT * FROM user_account_status")
    List<UserAccountStatus> findAll();

    @Select("SELECT count(*) FROM user_account_status")
    int findAllCount();

    @Select(value = "select * from user_account_status where id = #{id}")
    UserAccountStatus findById(int id);

    @Select("SELECT * FROM user_account_status where name =#{name}")
    UserAccountStatus findByName(@Param("name")String name);
}
