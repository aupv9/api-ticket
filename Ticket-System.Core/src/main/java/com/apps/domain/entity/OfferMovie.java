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
public class OfferMovie implements Serializable {
    private static final long serialVersionUID = 6578945996129399010L;
    private Integer offerId,movieId;
}
