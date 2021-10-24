package com.apps.mybatis.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SocialRepository {
    int insertGoogleAccount(@Param("userInfoId")Integer userInfoId,
                            @Param("googleId")String googleId);

}
