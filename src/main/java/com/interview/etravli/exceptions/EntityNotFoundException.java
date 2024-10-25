package com.interview.etravli.exceptions;

import com.interview.etravli.enums.ExceptionMessages;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(){
        super();
    }

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause){
        super(cause);
    }

    public EntityNotFoundException(ExceptionMessages message){
        super(message.toString());
    }

}
