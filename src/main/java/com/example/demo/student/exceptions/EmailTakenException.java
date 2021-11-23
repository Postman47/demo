package com.example.demo.student.exceptions;

public class EmailTakenException extends Exception{

    public static final String errorEmailTaken = "Error email arleady taken";

    public EmailTakenException(){
        super(errorEmailTaken);
    }

}
