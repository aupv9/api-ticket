package com.apps.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoogleLoginRequest implements Serializable {
    private static final long serialVersionUID = -3681369995534023801L;
    private String email,familyName, givenName,googleId, imageUrl,name;
}
