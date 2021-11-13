package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, roleId;
    private String firstName,email, lastName,activeDate, registeredAt, fullName, photo,lastLogin;
    private String timeZone;
    private Boolean isLoginSocial,currentLogged;

}
