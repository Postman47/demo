package com.example.demo.student.exceptions;

import lombok.Getter;

public class EmailTakenException extends Exception{

    @Getter
    public static final String errorEmailTaken = "Error email arleady taken";

    public EmailTakenException(){
        super(errorEmailTaken);
    }

}
