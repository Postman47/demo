package com.example.demo.signing.exceptions;

public class StudentNotSignedForcourseException extends Exception{
    public static final String NOT_SIGNED = "On this course there is no student ";

    public StudentNotSignedForcourseException(){
    }

    public StudentNotSignedForcourseException(String message) {
        super(message);
    }
}
