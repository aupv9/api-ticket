package com.apps.mapper;

import lombok.Data;

@Data
public class UserDto {
    private Integer id, modifiedBy,roleId, uasId;
    private String email,firstName,lastName;
    private String address, city, state, activeDate, registeredAt, password,photo;

}
