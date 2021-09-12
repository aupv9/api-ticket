package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_account", schema = "booksystem", catalog = "")
public class UserAccount implements Serializable {
    private int userInfoId;
    private String userName;
    private String email;
    private String password;
    private String passwordReminderToken;
    private Timestamp passwordReminderExpire;
    private String emailConfirmationToken;
    private Integer userAccountStatusId;
    private UserInfo userInfoByUserInfoId;
    private UserAccountStatus userAccountStatusByUserAccountStatusId;

    @Id
    @Column(name = "userInfoId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
    }

    @Basic
    @Column(name = "userName", nullable = true, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "passwordReminderToken", nullable = true, length = 255)
    public String getPasswordReminderToken() {
        return passwordReminderToken;
    }

    public void setPasswordReminderToken(String passwordReminderToken) {
        this.passwordReminderToken = passwordReminderToken;
    }

    @Basic
    @Column(name = "passwordReminderExpire", nullable = true)
    public Timestamp getPasswordReminderExpire() {
        return passwordReminderExpire;
    }

    public void setPasswordReminderExpire(Timestamp passwordReminderExpire) {
        this.passwordReminderExpire = passwordReminderExpire;
    }

    @Basic
    @Column(name = "emailConfirmationToken", nullable = true, length = 255)
    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
    }

    @Basic
    @Column(name = "user_account_status_id", nullable = true)
    public Integer getUserAccountStatusId() {
        return userAccountStatusId;
    }

    public void setUserAccountStatusId(Integer userAccountStatusId) {
        this.userAccountStatusId = userAccountStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userInfoId == that.userInfoId && Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(passwordReminderToken, that.passwordReminderToken) && Objects.equals(passwordReminderExpire, that.passwordReminderExpire) && Objects.equals(emailConfirmationToken, that.emailConfirmationToken) && Objects.equals(userAccountStatusId, that.userAccountStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoId, userName, email, password, passwordReminderToken, passwordReminderExpire, emailConfirmationToken, userAccountStatusId);
    }

    @OneToOne
    @JoinColumn(name = "userInfoId", referencedColumnName = "id", nullable = false,updatable = false, insertable = false)
    public UserInfo getUserInfoByUserInfoId() {
        return userInfoByUserInfoId;
    }

    public void setUserInfoByUserInfoId(UserInfo userInfoByUserInfoId) {
        this.userInfoByUserInfoId = userInfoByUserInfoId;
    }

    @ManyToOne
    @JoinColumn(name = "user_account_status_id", referencedColumnName = "id",updatable = false,insertable = false)
    public UserAccountStatus getUserAccountStatusByUserAccountStatusId() {
        return userAccountStatusByUserAccountStatusId;
    }

    public void setUserAccountStatusByUserAccountStatusId(UserAccountStatus userAccountStatusByUserAccountStatusId) {
        this.userAccountStatusByUserAccountStatusId = userAccountStatusByUserAccountStatusId;
    }
}
