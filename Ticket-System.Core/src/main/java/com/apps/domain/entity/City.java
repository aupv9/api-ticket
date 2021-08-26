package com.apps.domain.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "city")
public class City implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String state;

    private String country;
}
