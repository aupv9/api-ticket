package com.apps.service;


import com.apps.domain.entity.Reserved;
import com.apps.mapper.ReservedDto;

import java.util.List;

public interface TicketService {
    int reserved(List<Integer> seats, Integer user, Integer showtime, Integer room);
    boolean isReserved(Integer seat,Integer showTime,Integer room);
    Reserved findSeatReserved(Integer seat,Integer showTime,Integer room);
    List<ReservedDto> findByRoomShowTime(Integer room, Integer showTime);
}
