package com.apps.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowTimeDto implements Serializable {
    private static final long serialVersionUID = -7452136257489718362L;
    private String start,room;
    private String end;
    private Integer id;
    private String movie;
    private Integer theater;
    private double price;
    private boolean reshow;
}
