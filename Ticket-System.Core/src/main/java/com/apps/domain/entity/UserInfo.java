package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String firstName,email, lastName,activeDate, registeredAt, fullName, photo;
    private String timeZone;
    private Boolean isLoginSocial;

}
