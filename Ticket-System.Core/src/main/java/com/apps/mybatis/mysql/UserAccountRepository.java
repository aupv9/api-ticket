package com.apps.mybatis.mysql;

import com.apps.domain.entity.User;
import com.apps.domain.entity.UserAccount;
import com.apps.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserAccountRepository {
    int insert(@Param("user") UserAccount userAccount);
    int updateActive(@Param("active")Boolean aBoolean);
    List<User> findAllUser(@Param("limit") int limit, @Param("offset") int offset,
                           @Param("sort") String sort, @Param("order") String order,
                           @Param("search") String name,@Param("role") Integer role);
    int findCountAll( @Param("search") String name,@Param("role") Integer role);
    User findUserById(@Param("id")Integer id);

    int updateUserAccount(@Param("user")UserAccount userAccount);
    int updateUserInfo(@Param("user")UserInfo userInfo);

    User findUserByEmail(@Param("email")String email);
}
