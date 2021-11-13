package com.apps.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


@Data
public class ScheduleEmailRequest {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

//    @NotNull
//    private ZoneId timeZone;
}
