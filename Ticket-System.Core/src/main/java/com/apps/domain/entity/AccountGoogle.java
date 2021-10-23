package com.apps.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class AccountGoogle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int userInfoId;
    private String googleId;

}
