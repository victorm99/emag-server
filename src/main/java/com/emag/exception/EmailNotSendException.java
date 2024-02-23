package com.emag.exception;

public class EmailNotSendException extends RuntimeException{
    public EmailNotSendException(String msg){
        super(msg);
    }
}
