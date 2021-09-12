package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ticket_detail", schema = "booksystem", catalog = "")
@IdClass(TicketDetailPK.class)
public class TicketDetail implements Serializable {
    private int ticketId;
    private int seatRoomId;
    private Ticket ticketByTicketId;
    private SeatRoom seatRoomBySeatRoomId;

    @Id
    @Column(name = "ticket_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Id
    @Column(name = "seat_room_id", nullable = false)
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
        TicketDetail that = (TicketDetail) o;
        return ticketId == that.ticketId && seatRoomId == that.seatRoomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, seatRoomId);
    }

    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    public Ticket getTicketByTicketId() {
        return ticketByTicketId;
    }

    public void setTicketByTicketId(Ticket ticketByTicketId) {
        this.ticketByTicketId = ticketByTicketId;
    }

    @ManyToOne
    @JoinColumn(name = "seat_room_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public SeatRoom getSeatRoomBySeatRoomId() {
        return seatRoomBySeatRoomId;
    }

    public void setSeatRoomBySeatRoomId(SeatRoom seatRoomBySeatRoomId) {
        this.seatRoomBySeatRoomId = seatRoomBySeatRoomId;
    }
}
