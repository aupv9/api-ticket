package com.apps.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TicketDetailPK implements Serializable {
    private int ticketId;
    private int seatRoomId;

    @Column(name = "ticket_id", nullable = false)
    @Id
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Column(name = "seat_room_id", nullable = false)
    @Id
    public int getSeatRoomId() {
        return seatRoomId;
    }

    public void setSeatRoomId(int seatRoomId) {
        this.seatRoomId = seatRoomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDetailPK that = (TicketDetailPK) o;
        return ticketId == that.ticketId && seatRoomId == that.seatRoomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, seatRoomId);
    }
}
