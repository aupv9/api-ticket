package com.apps.service.impl;

import com.apps.domain.entity.Role;
import com.apps.domain.entity.User;
import com.apps.domain.entity.UserAccount;
import com.apps.domain.entity.UserInfo;
import com.apps.domain.repository.UserCustomRepository;
import com.apps.mapper.UserRegisterDto;
import com.apps.mybatis.mysql.RoleRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserCustomRepository userCustomRepository;

    private final UserAccountRepository userAccountRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;
//    @Value("${email.confirm.length}")
//    private Integer lengthEmailConfirmationToken;
//
//    @Value("${email.confirm.number}")
//    private Boolean isNumberToken;
//
//    @Value("${email.confirm.letters}")
//    private Boolean isLetters;

    public UserServiceImpl(UserCustomRepository userCustomRepository, UserAccountRepository userAccountRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userCustomRepository = userCustomRepository;
        this.userAccountRepository = userAccountRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException {
        UserInfo userInfo = UserInfo.builder()
                .email(userRegisterDto.getEmail())
                .isLoginSocial(userRegisterDto.getIsLoginSocial())
                .firstName(userRegisterDto.getFirstName())
                .lastName(userRegisterDto.getLastName())
                .fullName(userRegisterDto.getFirstName() + " " + userRegisterDto.getLastName())
                .build();
        String sqlInsertUserInfo = "insert into user_info(email,first_name,last_name,full_name,is_login_social) values(?,?,?,?,?)";
        int idReturned = this.userCustomRepository.insert(userInfo,sqlInsertUserInfo);
        if(idReturned > 0){
            String generatedToken = RandomStringUtils.random(30, true, true);
            UserAccount userAccount = UserAccount.builder()
                    .userInfoId(idReturned)
                    .email(userRegisterDto.getEmail())
                    .password(encoder.encode(userRegisterDto.getPassword()))
                    .emailConfirmationToken(generatedToken)
                    .build();
            int idUserReturned = this.userAccountRepository.insert(userAccount);
            Role role = this.roleRepository.findByName("ROLE_USER");
            this.roleRepository.insertUserRole(idReturned,role.getId());
            return idUserReturned;
        }
        return 0;
    }

    @Override
    public int activeUserAccount(UserAccount userAccount) {
        return this.userAccountRepository.updateActive(userAccount.getActive());
    }

    @Override
    public List<User> findAllUser(int limit, int offset, String sort, String order, String name, Integer role) {
        return this.userAccountRepository.findAllUser(limit,offset,sort,order,name,role > 0 ? role : null);
    }

    @Override
    public int findCountAll(String name,Integer role) {
        return this.userAccountRepository.findCountAll(name,role > 0 ? role : null);
    }

}
