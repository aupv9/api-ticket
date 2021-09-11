package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId;
    private String userName;
    private String email;
    private String password;
    private String passwordReminderToken;
    private Timestamp passwordReminderExpire;
    private String emailConfirmationToken;
    private int userAccountStatusId;
}
