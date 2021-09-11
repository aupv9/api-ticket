package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Seat implements Serializable {
    private int id;
    private Integer seatTypeId;
    private SeatType seatTypeBySeatTypeId;
    private Collection<SeatRoom> seatRoomsById;

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
    @Column(name = "seat_type_id", nullable = true)
    public Integer getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(Integer seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return id == seat.id && Objects.equals(seatTypeId, seat.seatTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seatTypeId);
    }

    @ManyToOne
    @JoinColumn(name = "seat_type_id", referencedColumnName = "id", updatable = false, insertable = false)
    public SeatType getSeatTypeBySeatTypeId() {
        return seatTypeBySeatTypeId;
    }

    public void setSeatTypeBySeatTypeId(SeatType seatTypeBySeatTypeId) {
        this.seatTypeBySeatTypeId = seatTypeBySeatTypeId;
    }

    @OneToMany(mappedBy = "seatByIdSeat")
    public Collection<SeatRoom> getSeatRoomsById() {
        return seatRoomsById;
    }

    public void setSeatRoomsById(Collection<SeatRoom> seatRoomsById) {
        this.seatRoomsById = seatRoomsById;
    }
}
