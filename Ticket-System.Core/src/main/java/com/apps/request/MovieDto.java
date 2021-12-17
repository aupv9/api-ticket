package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto implements Serializable {
    private static final long serialVersionUID = 783196685432106989L;

    private int id,durationMin;
    private String name,thumbnail,releasedDate,trailerUrl,genre;
    @NotNull
    @NotBlank
    private String photo;
    @NotNull
    @NotBlank
    private String casts;
    @NotNull
    @NotBlank
    private String tags;

}
