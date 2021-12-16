package com.apps.response;

import com.apps.domain.entity.Cast;
import com.apps.domain.entity.Tag;
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
public class MovieResponse implements Serializable {
    private static final long serialVersionUID = -1356449735687385309L;
    private int id,durationMin;
    private String name,thumbnail,releasedDate,trailerUrl,genre;
    private List<Cast> casts;
    private List<Tag> tags;
    private String photo;
}
