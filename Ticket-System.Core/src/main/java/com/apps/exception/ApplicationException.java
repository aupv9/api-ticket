package com.apps.exception;

import com.apps.contants.MessageData;
import lombok.Getter;


@Getter
public class ApplicationException extends Exception{

    private static final long serialVersionUID = 1L;

    private MessageData messageData;

    public ApplicationException(MessageData messageData) {
        super();
        this.messageData = messageData;
    }
}
