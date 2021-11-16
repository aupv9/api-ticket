package com.apps.response.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Integer id, modifiedBy, createdBy, uasId;
    private List<Integer> roleIds;
    private String email,firstName,lastName, lastLogin, createdDate,modifiedDate,fullName,photo;
    private String address, city, state, activeDate, registeredAt,password;
    private Boolean currentLogged;
}
