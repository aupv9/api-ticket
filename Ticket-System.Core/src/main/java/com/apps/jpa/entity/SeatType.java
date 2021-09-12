package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "seat_type", schema = "booksystem", catalog = "")
public class SeatType implements Serializable {
    private int id;
    private Collection<Seat> seatsById;

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
        SeatType seatType = (SeatType) o;
        return id == seatType.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @OneToMany(mappedBy = "seatTypeBySeatTypeId")
    public Collection<Seat> getSeatsById() {
        return seatsById;
    }

    public void setSeatsById(Collection<Seat> seatsById) {
        this.seatsById = seatsById;
    }
}
