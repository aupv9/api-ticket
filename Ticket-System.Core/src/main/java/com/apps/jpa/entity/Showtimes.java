package com.apps.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Showtimes implements Serializable {
    private int id;
    private Date dayshowtimes;
    private Collection<ShowtimesDetail> showtimesDetailsById;
    private Collection<Ticket> ticketsById;

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
    @Column(name = "dayshowtimes", nullable = true)
    public Date getDayshowtimes() {
        return dayshowtimes;
    }

    public void setDayshowtimes(Date dayshowtimes) {
        this.dayshowtimes = dayshowtimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showtimes showtimes = (Showtimes) o;
        return id == showtimes.id && Objects.equals(dayshowtimes, showtimes.dayshowtimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dayshowtimes);
    }

    @OneToMany(mappedBy = "showtimesByShowtimesId")
    public Collection<ShowtimesDetail> getShowtimesDetailsById() {
        return showtimesDetailsById;
    }

    public void setShowtimesDetailsById(Collection<ShowtimesDetail> showtimesDetailsById) {
        this.showtimesDetailsById = showtimesDetailsById;
    }

    @OneToMany(mappedBy = "showtimesByShowtimesId")
    public Collection<Ticket> getTicketsById() {
        return ticketsById;
    }

    public void setTicketsById(Collection<Ticket> ticketsById) {
        this.ticketsById = ticketsById;
    }
}
