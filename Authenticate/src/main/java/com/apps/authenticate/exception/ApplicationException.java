package com.apps.authenticate.exception;

import com.apps.authenticate.contants.MessageData;
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
