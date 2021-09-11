package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Room implements Serializable {
    private int id;
    private String code;
    private String name;
    private int theaterId;
    private Theater theaterByTheaterId;
    private Collection<SeatRoom> seatRoomsById;
    private Collection<Showtimes> showtimesById;
    private Collection<Tier> tiersById;

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
    @Column(name = "code", nullable = true, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "theater_id", nullable = false)
    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id && theaterId == room.theaterId && Objects.equals(code, room.code) && Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, theaterId);
    }

    @ManyToOne
    @JoinColumn(name = "theater_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Theater getTheaterByTheaterId() {
        return theaterByTheaterId;
    }

    public void setTheaterByTheaterId(Theater theaterByTheaterId) {
        this.theaterByTheaterId = theaterByTheaterId;
    }

    @OneToMany(mappedBy = "roomByIdRoom")
    public Collection<SeatRoom> getSeatRoomsById() {
        return seatRoomsById;
    }

    public void setSeatRoomsById(Collection<SeatRoom> seatRoomsById) {
        this.seatRoomsById = seatRoomsById;
    }

    @OneToMany(mappedBy = "roomByIdRoom")
    public Collection<Showtimes> getShowtimesById() {
        return showtimesById;
    }

    public void setShowtimesById(Collection<Showtimes> showtimesById) {
        this.showtimesById = showtimesById;
    }

    @OneToMany(mappedBy = "roomByRoomId")
    public Collection<Tier> getTiersById() {
        return tiersById;
    }

    public void setTiersById(Collection<Tier> tiersById) {
        this.tiersById = tiersById;
    }
}
