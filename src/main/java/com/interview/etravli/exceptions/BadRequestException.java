package com.interview.etravli.exceptions;

import com.interview.etravli.enums.ExceptionMessages;

public class BadRequestException extends RuntimeException{

    public BadRequestException(){
        super();
    }

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(String message, Throwable cause){
        super(message, cause);
    }

    public BadRequestException(Throwable cause){
        super(cause);
    }

    public BadRequestException(ExceptionMessages message){
        super(message.toString());
    }
}
