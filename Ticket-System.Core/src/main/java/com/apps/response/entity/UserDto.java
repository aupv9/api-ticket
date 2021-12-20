package com.apps.response.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = -597229628316865079L;
    private Integer id, modifiedBy, createdBy, uasId;
    private List<Integer> roleIds;
    private String email,firstName,lastName, lastLogin, createdDate,modifiedDate,fullName,photo;
    private String address, city, state, activeDate, registeredAt,password;
    private Boolean currentLogged;
}
