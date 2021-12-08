package com.apps.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie implements Serializable {

    private static final long serialVersionUID = 8878313134139905533L;
    private int id,durationMin;
    private String name,image,thumbnail,releasedDate,trailerUrl,genre;

}
