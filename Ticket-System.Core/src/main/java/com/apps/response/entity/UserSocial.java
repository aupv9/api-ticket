package com.apps.response.entity;

import lombok.Data;

@Data
public class UserSocial {
    private int id, roleId;
    private String firstName,email, lastName, fullName, photo,lastLogin;
    private Boolean isLoginSocial = false, currentLogged;
}
