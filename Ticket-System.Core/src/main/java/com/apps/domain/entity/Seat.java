package com.apps.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;


@Data
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Float price;
    private String seatType;
    private Integer roomId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return id == seat.id && Objects.equals(price, seat.price) && Objects.equals(seatType, seat.seatType) && Objects.equals(roomId, seat.roomId) && Objects.equals(numbers, seat.numbers) && Objects.equals(tier, seat.tier) && Objects.equals(isSelected, seat.isSelected) && Objects.equals(status, seat.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, seatType, roomId, numbers, tier, isSelected, status);
    }

    private Integer numbers;
    private String tier;
    private Boolean isSelected = true;
    private Integer status;
}
