package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomService implements Serializable {
    private static final long serialVersionUID = 6888781406922030195L;
    private int roomId,serviceId;
}
