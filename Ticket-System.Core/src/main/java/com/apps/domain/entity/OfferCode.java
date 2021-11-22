package com.apps.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferCode implements Serializable {

    private static final long serialVersionUID = -1864952352067129668L;
    private String code;
    private int offerId;
}

