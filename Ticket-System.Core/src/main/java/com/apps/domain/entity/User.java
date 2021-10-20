package com.apps.domain.entity;

import lombok.Data;

@Data
public class User {
    private Integer id, modifiedBy, createdBy,roleId, uasId;
    private String email,firstName,lastName, lastLogin, createdDate,modifiedDate;
    private String address, city, state, activeDate, registeredAt;
    private Boolean currentLogged;
}
