package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"showtimes_id","user_id",
        "seat_room_id"})
})
public class Ticket implements Serializable {
    private int id;
    private Integer showtimesId;
    private Integer seatRoomId;
    private int userId;
    private Collection<Payment> paymentsById;
    private Showtimes showtimesByShowtimesId;
    private SeatRoom seatRoomBySeatRoomId;
    private UserInfo userInfoByUserId;

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
    @Column(name = "showtimes_id", nullable = true)
    public Integer getShowtimesId() {
        return showtimesId;
    }

    public void setShowtimesId(Integer showtimesId) {
        this.showtimesId = showtimesId;
    }

    @Basic
    @Column(name = "seat_room_id", nullable = true)
    public Integer getSeatRoomId() {
        return seatRoomId;
    }

    public void setSeatRoomId(Integer seatRoomId) {
        this.seatRoomId = seatRoomId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && userId == ticket.userId && Objects.equals(showtimesId, ticket.showtimesId) && Objects.equals(seatRoomId, ticket.seatRoomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, showtimesId, seatRoomId, userId);
    }

    @OneToMany(mappedBy = "ticketByTicketId")
    public Collection<Payment> getPaymentsById() {
        return paymentsById;
    }

    public void setPaymentsById(Collection<Payment> paymentsById) {
        this.paymentsById = paymentsById;
    }

    @ManyToOne
    @JoinColumn(name = "showtimes_id", referencedColumnName = "id", insertable = false, updatable = false)
    public Showtimes getShowtimesByShowtimesId() {
        return showtimesByShowtimesId;
    }

    public void setShowtimesByShowtimesId(Showtimes showtimesByShowtimesId) {
        this.showtimesByShowtimesId = showtimesByShowtimesId;
    }

    @ManyToOne
    @JoinColumn(name = "seat_room_id", referencedColumnName = "id",
    updatable = false,insertable = false)
    public SeatRoom getSeatRoomBySeatRoomId() {
        return seatRoomBySeatRoomId;
    }

    public void setSeatRoomBySeatRoomId(SeatRoom seatRoomBySeatRoomId) {
        this.seatRoomBySeatRoomId = seatRoomBySeatRoomId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,
    insertable = false, updatable = false)
    public UserInfo getUserInfoByUserId() {
        return userInfoByUserId;
    }

    public void setUserInfoByUserId(UserInfo userInfoByUserId) {
        this.userInfoByUserId = userInfoByUserId;
    }
}
