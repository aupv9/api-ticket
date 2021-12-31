package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLetterDto implements Serializable {
    private static final long serialVersionUID = -6947234035087757105L;
    private String email;
    private int id;
}
