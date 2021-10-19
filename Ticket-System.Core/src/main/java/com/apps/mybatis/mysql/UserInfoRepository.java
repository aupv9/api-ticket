package com.apps.mybatis.mysql;


import com.apps.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoRepository {
    List<User> findAllUser(int limit, int offset, String sort, String order, int role);

}
