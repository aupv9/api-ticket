package com.apps.domain.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Foods implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private Timestamp createDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private Double price;
    private Integer category_id;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foods foods = (Foods) o;
        return id == foods.id && Objects.equals(name, foods.name) && Objects.equals(createDate, foods.createDate) && Objects.equals(startDate, foods.startDate) && Objects.equals(endDate, foods.endDate) && Objects.equals(price, foods.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createDate, startDate, endDate, price);
    }


}
