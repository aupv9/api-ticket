package com.apps.domain.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private Boolean active;
    private String fullName;
    private String roleId;
    private String lastLogin;
}
