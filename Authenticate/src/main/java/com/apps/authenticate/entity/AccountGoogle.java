package com.apps.authenticate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountGoogle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId;
    private String googleId;

}
