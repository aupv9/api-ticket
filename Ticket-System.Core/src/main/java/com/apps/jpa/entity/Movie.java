package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Movie implements Serializable {
    private int id;
    private Collection<ShowtimesDetail> showtimesDetailsById;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @OneToMany(mappedBy = "movieByMovieId")
    public Collection<ShowtimesDetail> getShowtimesDetailsById() {
        return showtimesDetailsById;
    }

    public void setShowtimesDetailsById(Collection<ShowtimesDetail> showtimesDetailsById) {
        this.showtimesDetailsById = showtimesDetailsById;
    }
}
