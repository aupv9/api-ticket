package com.apps.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RolePrivileges implements Serializable {
    private static final long serialVersionUID = 3402957888532814750L;
    private Integer roleId;
    private Integer privilegesId;
}
