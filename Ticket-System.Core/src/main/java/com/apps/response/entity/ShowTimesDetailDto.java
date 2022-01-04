package com.apps.response.entity;

import com.apps.domain.entity.Location;
import com.apps.domain.entity.Room;
import com.apps.domain.entity.Theater;
import com.apps.request.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowTimesDetailDto implements Serializable {
    private static final long serialVersionUID = -6300455129929864210L;
    private String timeStart,roomName,time;
    private String timeEnd,status,releasedDate;
    private Integer id,countSeatAvailable;
    private Integer movieId;
    private Integer roomId;
    private Integer theaterId;
    private double price;
    private boolean reShow;
    private RoomDto room;
    private Theater theater;
    private Location location;
}
