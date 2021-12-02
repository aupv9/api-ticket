package com.apps.service.impl;

import com.apps.contants.EmployeeStatus;
import com.apps.contants.UserStatus;
import com.apps.contants.Utilities;
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
import com.apps.response.entity.UserSocial;
import com.apps.service.EmployeeService;
import com.apps.service.UserService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@RequiredArgsConstructor
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

    private String sqlInsertUserInfo = "insert into user_info(email,first_name,last_name,full_name,is_login_social,photo) values(?,?,?,?,?,?)";

//    @Value("${email.confirm.length}")
//    private Integer lengthEmailConfirmationToken;
//
//    @Value("${email.confirm.number}")
//    private Boolean isNumberToken;
//
//    @Value("${email.confirm.letters}")
//    private Boolean isLetters;



    public int getTheaterManagerByUser(){
        var userId = this.getUserFromContext();
        int theaterId = 0;
        if(userId > 0){
            var employee = this.employeeService.findByUserId(userId);
            boolean isManager = false;
            if(employee.getId() > 0){
                var userRoles = this.roleService.findUserRoleById(employee.getUserId());
                for (var role : userRoles ){
                    var roleName = this.roleService.findRoleById(role.getRoleId());
                    if(roleName.getName().equals(com.apps.contants.Role.MANAGER.getName())){
                        isManager = true;
                        break;
                    }
                }
            }
            if(isManager) return  employee.getTheaterId();
            return theaterId;
        }
        return theaterId;
    }

    public boolean isManager(){
        var userId = this.getUserFromContext();
        if(userId > 0){
            var userRoles = this.roleService.findUserRoleById(userId);
            for (var role : userRoles ) {
                var roleName = this.roleService.findRoleById(role.getRoleId());
                if (roleName.getCode().equals(com.apps.contants.Role.MANAGER.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public UserDetails getAuthenticationFromContext() {
        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if((UserDetails) authentication.getPrincipal() != null){
            var userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
        }
        return  (UserDetails)authentication.getPrincipal();
    }

    @Override
    public int getTheaterByUser(){
        int userId = this.getUserFromContext();
        int theaterId = 0;
        if(userId > 0){
            var employee = this.employeeService.findByUserId(userId);
            theaterId = employee.getTheaterId();
        }
        return theaterId;
    }


    @Override
    public int registerAccountUser(UserRegisterDto userRegisterDto) throws SQLException {
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
            String generatedToken = RandomStringUtils.random(15, true, true);
            var createdBy = this.getUserFromContext();
            String passwordEncode = encoder.encode(userRegisterDto.getPassword());
            UserAccount userAccount = UserAccount.builder()
                    .userInfoId(idReturned)
                    .email(userRegisterDto.getEmail())
                    .password(passwordEncode)
                    .emailConfirmationToken(generatedToken)
                    .createdBy(createdBy)
                    .createdDate(getNowDateTime())
                    .registeredAt(getNowDateTime())
                    .address(userRegisterDto.getAddress())
                    .city(userRegisterDto.getCity()).state(userRegisterDto.getState())
                    .userAccountStatusId(statusRepository.findByCode(UserStatus.WAIT_CONFIRM.getName()).getId())
                    .build();
            int idUserReturned = this.userAccountRepository.insert(userAccount);

            Role role = this.roleRepository.findByCode(com.apps.contants.Role.USER.getName());
            this.roleRepository.insertUserRole(idReturned,role.getId());
            return idUserReturned;
        }
        return 0;
    }


    @Override
    public List<com.apps.response.entity.UserDto> findAllUser(int limit, int offset, String sort, String order, String name, Integer role) {
        var listUser = this.userAccountRepository.findAllUser(limit,offset,sort,order,name,role > 0 ? role : null);
        for (var user : listUser){
            var userRoles = this.roleRepository.findUserRoleById(user.getId());
            var roleIds = this.getAllRole(userRoles);
            user.setRoleIds(roleIds);
        }
        return listUser;
    }

    private List<Integer> getAllRole(List<UserRole> roles){
        return roles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<UserSocial> findAllUserSocial(int limit, int offset, String sort, String order, String name, Integer role) {
        var listUser = this.userAccountRepository.findAllUserSocial(limit,offset,sort,order,name,role > 0 ? role : null,1);
        for (var user : listUser){
            var userRoles = this.roleRepository.findUserRoleById(user.getId());
            var allRole = this.getAllRole(userRoles);
            user.setRoleIds(allRole);
        }
        return listUser;
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
    public com.apps.response.entity.UserDto findById(int id) {
        var user =  this.userAccountRepository.findUserById(id);
        var roles = this.roleRepository.findUserRoleById(id);
        var roleIds = roles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        if(user == null) {
            var userSocial = this.userAccountRepository.findUserSocialById(id);
            if (userSocial.getIsLoginSocial()) {
                user = new com.apps.response.entity.UserDto();
                user.setEmail(userSocial.getEmail());
                user.setRoleIds(roleIds);
                user.setUasId(2);
                user.setId(userSocial.getId());
                user.setFirstName(userSocial.getFirstName());
                user.setLastName(userSocial.getLastName());
                user.setFullName(userSocial.getFullName());
                user.setCurrentLogged(userSocial.getCurrentLogged());
                return user;
            }
        }
        assert user != null;
        user.setRoleIds(roleIds);
        return user;
    }

    @Override
    public int update(UserDto userDto) {
        UserInfo userInfo = UserInfo.builder()
                .id(userDto.getId()).firstName(userDto.getFirstName())
                .lastName(userDto.getLastName()).photo(userDto.getPhoto())
                .fullName(userDto.getFirstName() + " "+ userDto.getLastName())
                .build();
        this.userAccountRepository.updateUserInfo(userInfo);

        var userAccount = this.userAccountRepository.findUserById(userDto.getId());
        var modifiedBy = this.getUserFromContext();
        if(userAccount != null){
            UserAccount usAccount = UserAccount.builder()
                    .userInfoId(userInfo.getId())
                    .address(userDto.getAddress()).state(userDto.getAddress())
                    .city(userDto.getCity())
                    .modifiedDate(getNowDateTime())
                    .userAccountStatusId(userDto.getUasId())
                    .password(userDto.getPassword()!= null ? encoder.encode(userDto.getPassword()) : null )
                    .modifiedBy(modifiedBy)
                    .build();
            this.userAccountRepository.updateUserAccount(usAccount);
        }

        this.roleRepository.deleteUserRoleByUser(userDto.getId());

        for (var roleId: userDto.getRoleIds()){
            var role = this.roleRepository.findRoleById(roleId);
            if(role == null) {
                throw new NotFoundException("Not Role Have Id:" + role.getId());
            }
            this.roleRepository.insertUserRole(userDto.getId(),roleId);
            if(role.getName().equals(com.apps.contants.Role.STAFF.getName()) ||
                    role.getName().equals(com.apps.contants.Role.MANAGER.getName())){
                var employee = this.employeeService.findByUserId(userInfo.getId());
                if(employee != null){
                    employee.setRoleId(role.getId());
                    employee.setStatus(EmployeeStatus.Active.name());
                    this.employeeService.update(employee);
                }else{
                    this.employeeService
                            .insert(userInfo.getId(),role.getId(),modifiedBy,EmployeeStatus.New.name(),
                            getNowDateTime());
                }
            }
        }
        return userInfo.getId();
    }

    @Override
    public UserLoginResponse authenticate(String email, String password) throws JOSEException {
        var user = this.userAccountRepository.findUserByEmail(email);
        if(user == null){
            throw new NotFoundException("Invalid email or password");
        }
        var userStatus = this.statusRepository.findById(user.getUasId());
        if( encoder.matches(password,user.getPassword())
                && userStatus.getCode().equals(UserStatus.ACTIVE.getName())){
            var roles = this.roleRepository.findUserRoleById(user.getId());
            var userInfo = this.userAccountRepository.findUserByEmail(email);
            String token = this.jwtService.generatorToken(email);
            updateCurrentLogged(user.getId(),true);
            var privilege = roleService.getAuthorities(roles);
            return UserLoginResponse.builder()
                    .token(token)
                    .email(email).id(user.getId())
                    .fullName(userInfo.getFullName())
                    .photo(userInfo.getPhoto())
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
              var user = this.userAccountRepository.findUserSocialById(accountGoogle.getUserInfoId());
              var roles = this.roleRepository.findUserRoleById(accountGoogle.getUserInfoId());
              var privilege = roleService.getAuthorities(roles);
              String token = this.jwtService.generatorToken(googleLoginRequest.getEmail());
              updateCurrentLogged(accountGoogle.getUserInfoId(),true);
              return UserLoginResponse.builder().fullName(user.getFullName())
                      .photo(user.getPhoto()).id(user.getId())
                      .token(token).email(googleLoginRequest.getEmail())
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
                      .email(googleLoginRequest.getEmail())
                      .fullName(userInfo.getFullName())
                      .photo(userInfo.getPhoto())
                      .id(idReturned)
                      .privileges(privilege.stream()
                              .map(GrantedAuthority::getAuthority)
                              .collect(Collectors.toList()))
                      .build();
          }
    }

    @Override
    public int updateCurrentLogged(int userId,boolean isLogged){
        var userInfo = UserInfo.builder()
                .id(userId)
                .currentLogged(isLogged)
                .build();
       return  this.userAccountRepository.updateUserInfo(userInfo);
    }

    @Override
    public int updateCurrentLoggedByEmail(String email) {
        var user = this.userAccountRepository.findUserInfoByEmail(email);
        return this.updateCurrentLogged(user.getId(),false);
    }

    public int getUserFromContext() {
        int userId = 0;
        var userDetails = this.getAuthenticationFromContext();
        if( userDetails != null){
            String email = userDetails.getUsername();
            var user = this.userAccountRepository.findUserByEmail(email);
            if( user!= null){
                userId = user.getId();
            }else{
                var userInfo1 = this.userAccountRepository.findUserInfoByEmail(email);
                if(userInfo1 != null){
                    userId = userInfo1.getId();
                }
            }
        }
        return userId;
    }

    @Override
    public String getNowDateTime() {
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        return simpleDateFormat.format(localDateTime);
    }

    @Override
    public boolean checkEmailAlready(String email) {
        return this.userAccountRepository.findUserByEmail(email) != null;
    }


}
