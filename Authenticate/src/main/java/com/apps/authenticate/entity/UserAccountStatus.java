package com.apps.authenticate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String code;
    private String name;
}
