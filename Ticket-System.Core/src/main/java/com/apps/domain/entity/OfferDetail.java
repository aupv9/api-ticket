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
public class OfferDetail implements Serializable {
    private static final long serialVersionUID = -7857095928839035967L;
    private Integer offerId;
    private String code;
}
