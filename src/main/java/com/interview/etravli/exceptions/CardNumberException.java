package com.interview.etravli.exceptions;

import com.interview.etravli.enums.ExceptionMessages;

public class CardNumberException  extends RuntimeException{

    public CardNumberException(){
        super();
    }

    public CardNumberException(String message){
        super(message);
    }

    public CardNumberException(String message, Throwable cause){
        super(message, cause);
    }

    public CardNumberException(Throwable cause){
        super(cause);
    }

    public CardNumberException(ExceptionMessages message){
        super(message.toString());
    }
}
