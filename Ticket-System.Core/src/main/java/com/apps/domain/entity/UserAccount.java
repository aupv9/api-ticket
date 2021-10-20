package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId, modifiedBy, createdBy;
    private String email, password, passwordReminderToken, emailConfirmationToken,
             createdDate,modifiedDate,address, city, state, activeDate, registeredAt;
    private Timestamp passwordReminderExpire;
    private int userAccountStatusId;
    private Boolean active;

}
