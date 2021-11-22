package com.example.demo.student.exceptions;

public class EmailTakenException extends Exception{

    public EmailTakenException(String errorMessage){
        super(errorMessage);
    }

}
