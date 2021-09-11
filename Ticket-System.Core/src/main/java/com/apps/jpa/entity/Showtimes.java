package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(columnNames = {"id_movie","id_room","time_start","time_start"})})
public class Showtimes implements Serializable {
    private int id;
    private int idRoom;
    private int idMovie;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private Room roomByIdRoom;
    private Movie movieByIdMovie;
    private Collection<Ticket> ticketsById;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "id_room", nullable = false)
    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    @Basic
    @Column(name = "id_movie", nullable = false)
    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    @Basic
    @Column(name = "time_start", nullable = false)
    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    @Basic
    @Column(name = "time_end", nullable = false)
    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showtimes showtimes = (Showtimes) o;
        return id == showtimes.id && idRoom == showtimes.idRoom && idMovie == showtimes.idMovie && Objects.equals(timeStart, showtimes.timeStart) && Objects.equals(timeEnd, showtimes.timeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idRoom, idMovie, timeStart, timeEnd);
    }

    @ManyToOne
    @JoinColumn(name = "id_room", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Room getRoomByIdRoom() {
        return roomByIdRoom;
    }

    public void setRoomByIdRoom(Room roomByIdRoom) {
        this.roomByIdRoom = roomByIdRoom;
    }

    @ManyToOne
    @JoinColumn(name = "id_movie", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Movie getMovieByIdMovie() {
        return movieByIdMovie;
    }

    public void setMovieByIdMovie(Movie movieByIdMovie) {
        this.movieByIdMovie = movieByIdMovie;
    }

    @OneToMany(mappedBy = "showtimesByShowtimesId")
    public Collection<Ticket> getTicketsById() {
        return ticketsById;
    }

    public void setTicketsById(Collection<Ticket> ticketsById) {
        this.ticketsById = ticketsById;
    }
}
