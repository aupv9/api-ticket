package com.apps.service.impl;

import com.apps.domain.entity.*;
import com.apps.domain.repository.UserCustomRepository;
import com.apps.exception.NotFoundException;
import com.apps.filter.JWTService;
import com.apps.mapper.UserDto;
import com.apps.mapper.UserRegisterDto;
import com.apps.mybatis.mysql.RoleRepository;
import com.apps.mybatis.mysql.SocialRepository;
import com.apps.mybatis.mysql.UserAccountRepository;
import com.apps.mybatis.mysql.UserAccountStatusRepository;
import com.apps.request.GoogleLoginRequest;
import com.apps.response.UserLoginResponse;
import com.apps.service.EmployeeService;
import com.apps.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserCustomRepository userCustomRepository;

    private final UserAccountRepository userAccountRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final UserAccountStatusRepository statusRepository;

    private final RoleServiceImpl roleService;

    private final JWTService jwtService;

    private final EmployeeService employeeService;

    private final SocialRepository socialRepository;

    private DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    private String sqlInsertUserInfo = "insert into user_info(email,first_name,last_name,full_name,is_login_social,photo) values(?,?,?,?,?,?)";

//    @Value("${email.confirm.length}")
//    private Integer lengthEmailConfirmationToken;
//
//    @Value("${email.confirm.number}")
//    private Boolean isNumberToken;
//
//    @Value("${email.confirm.letters}")
//    private Boolean isLetters;

    public UserServiceImpl(UserCustomRepository userCustomRepository, UserAccountRepository userAccountRepository, PasswordEncoder encoder, RoleRepository roleRepository, UserAccountStatusRepository statusRepository, RoleServiceImpl roleService, JWTService jwtService, EmployeeService employeeService, SocialRepository socialRepository) {
        this.userCustomRepository = userCustomRepository;
        this.userAccountRepository = userAccountRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.employeeService = employeeService;
        this.socialRepository = socialRepository;
    }

    @Override
    public int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.now();
        UserInfo userInfo = UserInfo.builder()
                .email(userRegisterDto.getEmail())
                .isLoginSocial(userRegisterDto.getIsLoginSocial())
                .firstName(userRegisterDto.getFirstName())
                .lastName(userRegisterDto.getLastName())
                .fullName(userRegisterDto.getFirstName() + " " + userRegisterDto.getLastName())
                .photo(userRegisterDto.getPhoto())
                .build();
        int idReturned = this.userCustomRepository.insert(userInfo,sqlInsertUserInfo);
        if(idReturned > 0){
            LocalDateTime createDate = LocalDateTime.now();
            String generatedToken = RandomStringUtils.random(15, true, true);
            var authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (UserDetails)authentication.getPrincipal();
            int createdBy = 0;

            if(userDetails != null){
                String email = userDetails.getUsername();
                createdBy = this.userAccountRepository.findUserByEmail(email).getId();
            }
            String passwordEncode = encoder.encode(userRegisterDto.getPassword());
            UserAccount userAccount = UserAccount.builder()
                    .userInfoId(idReturned)
                    .email(userRegisterDto.getEmail())
                    .password(passwordEncode)
                    .emailConfirmationToken(generatedToken)
                    .createdBy(createdBy)
                    .createdDate(createDate.format(simpleDateFormat))
                    .registeredAt(localDateTime.format(simpleDateFormat))
                    .address(userRegisterDto.getAddress())
                    .city(userRegisterDto.getCity()).state(userRegisterDto.getState())
                    .userAccountStatusId(statusRepository.findByName("NONE ACTIVE").getId())
                    .build();
            int idUserReturned = this.userAccountRepository.insert(userAccount);
            Role role = this.roleRepository.findByName("ROLE_USER");
            this.roleRepository.insertUserRole(idReturned,role.getId());
            return idUserReturned;
        }
        return 0;
    }


    @Override
    public List<User> findAllUser(int limit, int offset, String sort, String order, String name, Integer role) {
        return this.userAccountRepository.findAllUser(limit,offset,sort,order,name,role > 0 ? role : null);
    }

    @Override
    public List<UserInfo> findAllUserSocial(int limit, int offset, String sort, String order, String name, Integer role) {
        return this.userAccountRepository.findAllUserSocial(limit,offset,sort,order,name,role > 0 ? role : null);
    }

    @Override
    public int findCountAll(String name,Integer role) {
        return this.userAccountRepository.findCountAll(name,role > 0 ? role : null);
    }

    @Override
    public int findCountAllSocial(String name, Integer role) {
        return this.userAccountRepository.findCountAllSocial(name,role > 0 ? role : null);
    }

    @Override
    public User findById(int id) {
        return this.userAccountRepository.findUserById(id);
    }

    @Override
    public int update(UserDto userDto) {
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        UserInfo userInfo = UserInfo.builder()
                .id(userDto.getId()).firstName(userDto.getFirstName())
                .lastName(userDto.getLastName()).photo(userDto.getPhoto())
                .fullName(userDto.getFirstName() + " "+ userDto.getLastName())
                .build();
        this.userAccountRepository.updateUserInfo(userInfo);
        var authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails)authentication.getPrincipal();
        int modifiedBy = 0;
        if(userDetails != null){
            String email = userDetails.getUsername();
            var user = this.userAccountRepository.findUserByEmail(email);
            if( user!= null){
                modifiedBy = user.getId();
            }else{
                var userInfo1 = this.userAccountRepository.findUserInfoByEmail(email);
                if(userInfo1 != null){
                    modifiedBy = userInfo1.getId();
                }
            }
        }
        UserAccount userAccount = UserAccount.builder()
                .userInfoId(userInfo.getId())
                .address(userDto.getAddress()).state(userDto.getAddress())
                .city(userDto.getCity())
                .modifiedDate(localDateTime.format(simpleDateFormat))
                .userAccountStatusId(userDto.getUasId())
                .password(userDto.getPassword())
                .modifiedBy(modifiedBy)
                .build();
        this.userAccountRepository.updateUserAccount(userAccount);
        Role role  = roleRepository.findRoleById(userDto.getRoleId());
        if(role == null) throw new NotFoundException("Not Role Have Id:" + role.getId());

        this.roleRepository.updateRoleByUser(userDto.getId(),userDto.getRoleId());

        if(role.getName().equals("ROLE_STAFF") || role.getName().equals("Manager_Theater")){
            var employee = this.employeeService.findByUserId(userInfo.getId());
            if(employee != null){
                employee.setRoleId(role.getId());
                this.employeeService.update(employee);
            }else{
                this.employeeService.insert(userInfo.getId(),role.getId(),modifiedBy,"New",localDateTime.format(simpleDateFormat));
            }
        }
        return 1;
    }

    @Override
    public UserLoginResponse authenticate(String email, String password) throws JOSEException {
        var user = this.userAccountRepository.findUserByEmail(email);
        if(user == null){
            throw new NotFoundException("Invalid email or password");
        }
        if(encoder.matches(password,user.getPassword()) && user.getUasId() >= 2 && user.getUasId() <= 3){
            var roles = this.roleRepository.findUserRoleById(user.getId());
            String token = this.jwtService.generatorToken(email);
            var privilege = roleService.getAuthorities(roles);
            return UserLoginResponse.builder()
                    .token(token)
                    .privileges(privilege.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .build();
        }
        return  UserLoginResponse.builder()
                .build();
    }

    @Override
    public UserLoginResponse authenticateWithGoogle(GoogleLoginRequest googleLoginRequest) throws JOSEException, SQLException {

          var accountGoogle = this.userAccountRepository.findUserByGoogleAccount(googleLoginRequest.getGoogleId());
          if(accountGoogle != null){
              var roles = this.roleRepository.findUserRoleById(accountGoogle.getUserInfoId());
              var privilege = roleService.getAuthorities(roles);
              String token = this.jwtService.generatorToken(googleLoginRequest.getEmail());
              return UserLoginResponse.builder()
                    .token(token)
                    .privileges(privilege.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .build();
          }else{
              UserInfo userInfo = UserInfo.builder()
                      .email(googleLoginRequest.getEmail())
                      .isLoginSocial(true)
                      .firstName(googleLoginRequest.getFamilyName())
                      .lastName(googleLoginRequest.getGivenName())
                      .fullName(googleLoginRequest.getName())
                      .photo(googleLoginRequest.getImageUrl())
                      .build();
              int idReturned = this.userCustomRepository.insert(userInfo,sqlInsertUserInfo);
              Role role = this.roleRepository.findByName("ROLE_USER");
              this.roleRepository.insertUserRole(idReturned,role.getId());

              this.socialRepository.insertGoogleAccount(idReturned,googleLoginRequest.getGoogleId());

              assert accountGoogle != null;
              var roles = this.roleRepository.findUserRoleById(idReturned);
              var privilege = roleService.getAuthorities(roles);
              String token = this.jwtService.generatorToken(googleLoginRequest.getEmail());
              return UserLoginResponse.builder()
                      .token(token)
                      .privileges(privilege.stream()
                              .map(GrantedAuthority::getAuthority)
                              .collect(Collectors.toList()))
                      .build();
          }
    }


}