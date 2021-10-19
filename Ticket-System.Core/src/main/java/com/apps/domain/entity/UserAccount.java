package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId;
    private String email;
    private String password;
    private String passwordReminderToken;
    private Timestamp passwordReminderExpire;
    private String emailConfirmationToken;
    private int userAccountStatusId;
    private String status;
    private Boolean active;
}
