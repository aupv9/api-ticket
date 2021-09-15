package com.apps.domain.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class TicketDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private int ticketId;
    private Integer seatRoomId;
}
