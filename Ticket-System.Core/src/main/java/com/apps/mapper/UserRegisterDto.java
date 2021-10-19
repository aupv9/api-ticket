package com.apps.mapper;

import lombok.Data;

@Data
public class UserRegisterDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String timeZone;
    private Boolean isLoginSocial = false;
    private String password;
    private String phoneNumber;
}
