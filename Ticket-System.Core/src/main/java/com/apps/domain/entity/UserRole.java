package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {
    private static final long serialVersionUID = -6478037583182903652L;
    private Integer userId;
    private Integer roleId;
}
