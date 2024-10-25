package com.interview.etravli.exceptions;

import com.interview.etravli.enums.ExceptionMessages;

public class AuthorizationException extends RuntimeException{

    public AuthorizationException(){
        super();
    }

    public AuthorizationException(String message){
        super(message);
    }

    public AuthorizationException(String message, Throwable cause){
        super(message, cause);
    }

    public AuthorizationException(Throwable cause){
        super(cause);
    }

    public AuthorizationException(ExceptionMessages message){
        super(message.toString());
    }

}
