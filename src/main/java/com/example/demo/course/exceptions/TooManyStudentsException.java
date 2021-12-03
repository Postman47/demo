package com.example.demo.course.exceptions;

public class TooManyStudentsException extends RuntimeException{
    public static final String TOO_MANY_STUDENTS = "There is no places in this course";

    public TooManyStudentsException(){
    }

    public TooManyStudentsException(String message) {
        super(message);
    }
}
