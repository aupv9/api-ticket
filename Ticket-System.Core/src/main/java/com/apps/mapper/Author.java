package com.apps.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author implements Serializable {
    private static final long serialVersionUID = 20453569338997823L;
    private int id;
    private String avatar,fullName;
}
