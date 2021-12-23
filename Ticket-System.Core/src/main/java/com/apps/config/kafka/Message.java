package com.apps.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 650466318779995881L;
    private String domain;
    private Integer id;
    private List<Object> payload;

    public Message(String domain, List<Object> payload) {
        this.domain = domain;
        this.payload = payload;
    }
}
//    private String domain;
//    private Object payload;