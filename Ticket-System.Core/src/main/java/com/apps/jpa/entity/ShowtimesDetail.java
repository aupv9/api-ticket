package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "showtimes_detail", schema = "booksystem", catalog = "")
@IdClass(ShowtimesDetailPK.class)
public class ShowtimesDetail implements Serializable {
    private int showtimesId;
    private int movieId;
    private int locationId;
    private int theaterId;
    private int roomId;
    private String timeStart;
    private Showtimes showtimesByShowtimesId;
    private Movie movieByMovieId;
    private Location locationByLocationId;
    private Theater theaterByTheaterId;
    private Room roomByRoomId;

    @Id
    @Column(name = "showtimes_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getShowtimesId() {
        return showtimesId;
    }

    public void setShowtimesId(int showtimesId) {
        this.showtimesId = showtimesId;
    }

    @Id
    @Column(name = "movie_id", nullable = false)
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Id
    @Column(name = "location_id", nullable = false)
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Id
    @Column(name = "theater_id", nullable = false)
    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    @Id
    @Column(name = "room_id", nullable = false)
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Basic
    @Column(name = "time_start", nullable = true, length = 100)
    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowtimesDetail that = (ShowtimesDetail) o;
        return showtimesId == that.showtimesId && movieId == that.movieId && locationId == that.locationId && theaterId == that.theaterId && roomId == that.roomId && Objects.equals(timeStart, that.timeStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showtimesId, movieId, locationId, theaterId, roomId, timeStart);
    }

    @ManyToOne
    @JoinColumn(name = "showtimes_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Showtimes getShowtimesByShowtimesId() {
        return showtimesByShowtimesId;
    }

    public void setShowtimesByShowtimesId(Showtimes showtimesByShowtimesId) {
        this.showtimesByShowtimesId = showtimesByShowtimesId;
    }

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Movie getMovieByMovieId() {
        return movieByMovieId;
    }

    public void setMovieByMovieId(Movie movieByMovieId) {
        this.movieByMovieId = movieByMovieId;
    }

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Location getLocationByLocationId() {
        return locationByLocationId;
    }

    public void setLocationByLocationId(Location locationByLocationId) {
        this.locationByLocationId = locationByLocationId;
    }

    @ManyToOne
    @JoinColumn(name = "theater_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Theater getTheaterByTheaterId() {
        return theaterByTheaterId;
    }

    public void setTheaterByTheaterId(Theater theaterByTheaterId) {
        this.theaterByTheaterId = theaterByTheaterId;
    }

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Room getRoomByRoomId() {
        return roomByRoomId;
    }

    public void setRoomByRoomId(Room roomByRoomId) {
        this.roomByRoomId = roomByRoomId;
    }
}
