package com.apps.service.impl;

import com.apps.domain.entity.Reserved;
import com.apps.mapper.ReservedDto;
import com.apps.mybatis.mysql.TicketRepository;
import com.apps.service.TicketService;
import com.apps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final UserService userService;

    public TicketServiceImpl(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }


    @Override
    public int reserved(List<Integer> seats, Integer user, Integer showtime, Integer room) {
        if(user == 0){
            user = this.userService.getUserFromContext();
        }
        for (Integer seat: seats){
            if(!this.isReserved(seat,showtime,room)){
                this.ticketRepository.reserved(seat, user, showtime,room);
            }
        }
        return 1;
    }

    @Override
    public boolean isReserved(Integer seat, Integer showTime, Integer room) {
        return this.ticketRepository.isReserved(seat,showTime,room) > 0;
    }

    @Override
    public Reserved findSeatReserved(Integer seat, Integer showTime, Integer room) {
        return this.ticketRepository.findSeatReserved( seat, showTime, room);
    }

    @Override
    public List<ReservedDto> findByRoomShowTime(Integer room, Integer showTime) {
        return this.ticketRepository.findByRoomShowTime(room,showTime);
    }

}
