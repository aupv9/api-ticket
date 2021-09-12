package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Tier implements Serializable {
    private int id;
    private String code;
    private String name;
    private int roomId;
    private Collection<SeatRoom> seatRoomsById;
    private Room roomByRoomId;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "room_id", nullable = false)
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
        Tier tier = (Tier) o;
        return id == tier.id && roomId == tier.roomId && Objects.equals(code, tier.code) && Objects.equals(name, tier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, roomId);
    }

    @OneToMany(mappedBy = "tierByIdTier")
    public Collection<SeatRoom> getSeatRoomsById() {
        return seatRoomsById;
    }

    public void setSeatRoomsById(Collection<SeatRoom> seatRoomsById) {
        this.seatRoomsById = seatRoomsById;
    }

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id",
            nullable = false, updatable = false, insertable = false)
    public Room getRoomByRoomId() {
        return roomByRoomId;
    }

    public void setRoomByRoomId(Room roomByRoomId) {
        this.roomByRoomId = roomByRoomId;
    }
}
