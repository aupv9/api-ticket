package com.apps.mapper;

import lombok.Data;

@Data
public class UserRegisterDto {
    private Integer id, createBy,modifiedBy;
    private String email,firstName,lastName,password,phoneNumber,photo;
    private String timeZone;
    private Boolean isLoginSocial = false;
    private String address, city, state;
}
