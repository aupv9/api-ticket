package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_info", schema = "booksystem", catalog = "")
public class UserInfo implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String timeZone;
    private Byte isLoginSocial;
    private UserAccount userAccountById;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = true, length = 255)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 255)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "full_name", nullable = true, length = 500)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
    @Column(name = "time_zone", nullable = true, length = 100)
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Basic
    @Column(name = "is_login_social", nullable = true)
    public Byte getIsLoginSocial() {
        return isLoginSocial;
    }

    public void setIsLoginSocial(Byte isLoginSocial) {
        this.isLoginSocial = isLoginSocial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id && Objects.equals(firstName, userInfo.firstName) && Objects.equals(lastName, userInfo.lastName) && Objects.equals(fullName, userInfo.fullName) && Objects.equals(email, userInfo.email) && Objects.equals(timeZone, userInfo.timeZone) && Objects.equals(isLoginSocial, userInfo.isLoginSocial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, fullName, email, timeZone, isLoginSocial);
    }

    @OneToOne(mappedBy = "userInfoByUserInfoId")
    public UserAccount getUserAccountById() {
        return userAccountById;
    }

    public void setUserAccountById(UserAccount userAccountById) {
        this.userAccountById = userAccountById;
    }
}
