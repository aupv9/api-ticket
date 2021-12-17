package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cast implements Serializable {
    private static final long serialVersionUID = 7205270685990923227L;
    private Integer id;
    private String name, profile,role;
}
