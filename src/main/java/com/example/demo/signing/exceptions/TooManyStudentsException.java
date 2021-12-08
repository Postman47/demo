package com.example.demo.signing.exceptions;

public class TooManyStudentsException extends Exception{
    public static final String TOO_MANY_STUDENTS = "There is no places in this course";

    public TooManyStudentsException(){
    }

    public TooManyStudentsException(String message) {
        super(message);
    }
}
