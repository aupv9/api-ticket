package com.apps.domain.entity;

import lombok.Data;

import java.util.List;

@Data
public class Reserved {
    private Integer  user, showTime,room;
    private List<Integer> seats;
}
