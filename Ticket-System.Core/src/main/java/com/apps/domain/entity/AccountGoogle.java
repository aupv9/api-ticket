package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountGoogle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId;
    private String googleId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountFacebook implements Serializable {
        private static final long serialVersionUID = 1L;
        private int userInfoId;
        private String facebookId;
        private Timestamp createdDate;
        private Timestamp updatedDate;
    }
}
