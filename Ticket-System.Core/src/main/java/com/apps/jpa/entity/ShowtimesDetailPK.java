package com.apps.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ShowtimesDetailPK implements Serializable {
    private int showtimesId;
    private int movieId;
    private int locationId;
    private int theaterId;
    private int roomId;

    @Column(name = "showtimes_id", nullable = false)
    @Id
    public int getShowtimesId() {
        return showtimesId;
    }

    public void setShowtimesId(int showtimesId) {
        this.showtimesId = showtimesId;
    }

    @Column(name = "movie_id", nullable = false)
    @Id
    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    @Column(name = "location_id", nullable = false)
    @Id
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Column(name = "theater_id", nullable = false)
    @Id
    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    @Column(name = "room_id", nullable = false)
    @Id
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowtimesDetailPK that = (ShowtimesDetailPK) o;
        return showtimesId == that.showtimesId && movieId == that.movieId && locationId == that.locationId && theaterId == that.theaterId && roomId == that.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(showtimesId, movieId, locationId, theaterId, roomId);
    }
}
