package com.example.demo.student.exceptions;


public class EmailTakenException extends Exception{

    public static final String EMAIL_TAKEN_EXCEPTION = "Error email arleady taken";

    public EmailTakenException(){

    }

    public EmailTakenException(String message) {
        super(message);
    }
}
