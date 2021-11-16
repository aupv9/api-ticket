package com.apps.response.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserSocial {
    private int id;
    private List<Integer> roleIds;
    private String firstName,email, lastName, fullName, photo,lastLogin;
    private Boolean isLoginSocial = false, currentLogged;
}
