package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto implements Serializable {
    private static final long serialVersionUID = 783196685432106989L;
    private int id,durationMin;
    private String name,thumbnail,releasedDate,trailerUrl,genre;
    private String photo,casts,tags;

}
