package com.apps.domain.entity;

import lombok.Data;

@Data
public class User {
    private Integer id, modifiedBy, createdBy,roleId, uasId;
    private String email,firstName,lastName, lastLogin, createdDate,modifiedDate,fullName,photo;
    private String address, city, state, activeDate, registeredAt,password;
    private Boolean currentLogged;
}
