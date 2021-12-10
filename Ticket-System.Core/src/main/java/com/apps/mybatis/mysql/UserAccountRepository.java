package com.apps.mybatis.mysql;

import com.apps.domain.entity.AccountGoogle;
import com.apps.domain.entity.User;
import com.apps.domain.entity.UserAccount;
import com.apps.domain.entity.UserInfo;
import com.apps.response.entity.UserDto;
import com.apps.response.entity.UserSocial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserAccountRepository {
    int insert(@Param("user") UserAccount userAccount);
    int updateActive(@Param("active")Boolean aBoolean);
    List<UserDto> findAllUser(@Param("limit") int limit, @Param("offset") int offset,
                           @Param("sort") String sort, @Param("order") String order,
                           @Param("search") String name,@Param("role") Integer role);
    List<UserSocial> findAllUserSocial(@Param("limit") int limit, @Param("offset") int offset,
                                       @Param("sort") String sort, @Param("order") String order,
                                       @Param("search") String name, @Param("role") Integer role,
                                       @Param("social") Integer social);

    UserSocial findUserSocialById(@Param("id")Integer id);


    int findCountAll( @Param("search") String name,@Param("role") Integer role);

    @Select("select count(*) from user_info")
    int findCountAllSocial( @Param("search") String name,@Param("role") Integer role);


    UserDto findUserById(@Param("id")Integer id);

    @Select("select * from user_account where email_confirmation_token = #{token}")
    User findUserByTokenEmail(@Param("token")String token);

    int updateUserAccount(@Param("user")UserAccount userAccount);
    int updateUserInfo(@Param("user")UserInfo userInfo);

    User findUserByEmail(@Param("email")String email);

    UserSocial findUserInfoByEmail(@Param("email")String email);

    @Select("select * from google_account where google_id = #{googleId}")
    AccountGoogle findUserByGoogleAccount(@Param("googleId")String googleId);

    @Update("update user_account set status = #{status} where user_info_id = #{id}")
    int activeUser(@Param("id")Integer idUser,@Param("status")Integer status);
}
