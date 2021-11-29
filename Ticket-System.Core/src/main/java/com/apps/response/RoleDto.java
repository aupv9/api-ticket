package com.apps.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 7989440210970344270L;
    private Integer id;
    private String code,name;
    private List<Integer> privileges;
}
