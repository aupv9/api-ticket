package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "seat_room",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_room","id_seat","id_tier"})})
public class SeatRoom implements Serializable {
    private int id;
    private Integer idRoom;
    private Integer idSeat;
    private Boolean status;
    private Integer idTier;
    private Room roomByIdRoom;
    private Seat seatByIdSeat;
    private Tier tierByIdTier;
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
    @Column(name = "id_room", nullable = true)
    public Integer getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Integer idRoom) {
        this.idRoom = idRoom;
    }

    @Basic
    @Column(name = "id_seat", nullable = true)
    public Integer getIdSeat() {
        return idSeat;
    }

    public void setIdSeat(Integer idSeat) {
        this.idSeat = idSeat;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Basic
    @Column(name = "id_tier", nullable = true)
    public Integer getIdTier() {
        return idTier;
    }

    public void setIdTier(Integer idTier) {
        this.idTier = idTier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatRoom seatRoom = (SeatRoom) o;
        return id == seatRoom.id && Objects.equals(idRoom, seatRoom.idRoom) && Objects.equals(idSeat, seatRoom.idSeat) && Objects.equals(status, seatRoom.status) && Objects.equals(idTier, seatRoom.idTier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idRoom, idSeat, status, idTier);
    }

    @ManyToOne
    @JoinColumn(name = "id_room", referencedColumnName = "id", insertable = false, updatable = false)
    public Room getRoomByIdRoom() {
        return roomByIdRoom;
    }

    public void setRoomByIdRoom(Room roomByIdRoom) {
        this.roomByIdRoom = roomByIdRoom;
    }

    @ManyToOne
    @JoinColumn(name = "id_seat", referencedColumnName = "id", updatable = false, insertable = false)
    public Seat getSeatByIdSeat() {
        return seatByIdSeat;
    }

    public void setSeatByIdSeat(Seat seatByIdSeat) {
        this.seatByIdSeat = seatByIdSeat;
    }

    @ManyToOne
    @JoinColumn(name = "id_tier", referencedColumnName = "id", insertable = false, updatable = false)
    public Tier getTierByIdTier() {
        return tierByIdTier;
    }

    public void setTierByIdTier(Tier tierByIdTier) {
        this.tierByIdTier = tierByIdTier;
    }

    @OneToMany(mappedBy = "seatRoomBySeatRoomId")
    public Collection<Ticket> getTicketsById() {
        return ticketsById;
    }

    public void setTicketsById(Collection<Ticket> ticketsById) {
        this.ticketsById = ticketsById;
    }
}
