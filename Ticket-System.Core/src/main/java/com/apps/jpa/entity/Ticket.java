package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Ticket implements Serializable {
    private int id;
    private Integer showtimesId;
    private int userId;
    private Date ticketDate;
    private Integer movie;
    private Integer location;
    private Integer theater;
    private Integer room;
    private Collection<Payment> paymentsById;
    private Showtimes showtimesByShowtimesId;
    private Collection<TicketDetail> ticketDetailsById;

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
    @Column(name = "showtimes_id", nullable = true)
    public Integer getShowtimesId() {
        return showtimesId;
    }

    public void setShowtimesId(Integer showtimesId) {
        this.showtimesId = showtimesId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "ticket_date", nullable = true)
    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    @Basic
    @Column(name = "movie", nullable = true)
    public Integer getMovie() {
        return movie;
    }

    public void setMovie(Integer movie) {
        this.movie = movie;
    }

    @Basic
    @Column(name = "location", nullable = true)
    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    @Basic
    @Column(name = "theater", nullable = true)
    public Integer getTheater() {
        return theater;
    }

    public void setTheater(Integer theater) {
        this.theater = theater;
    }

    @Basic
    @Column(name = "room", nullable = true)
    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && userId == ticket.userId && Objects.equals(showtimesId, ticket.showtimesId) && Objects.equals(ticketDate, ticket.ticketDate) && Objects.equals(movie, ticket.movie) && Objects.equals(location, ticket.location) && Objects.equals(theater, ticket.theater) && Objects.equals(room, ticket.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, showtimesId, userId, ticketDate, movie, location, theater, room);
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

    @OneToMany(mappedBy = "ticketByTicketId")
    public Collection<TicketDetail> getTicketDetailsById() {
        return ticketDetailsById;
    }

    public void setTicketDetailsById(Collection<TicketDetail> ticketDetailsById) {
        this.ticketDetailsById = ticketDetailsById;
    }
}
