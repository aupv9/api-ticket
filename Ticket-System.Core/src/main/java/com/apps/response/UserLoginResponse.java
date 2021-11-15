package com.apps.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class UserLoginResponse {
    private List<String> privileges;
    private String email,photo,fullName,token;
    private int id;
}
