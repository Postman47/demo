package com.example.demo.course.exceptions;

public class NameTakenException extends Exception{
    public static final String NAME_TAKEN_EXCEPTION = "Course with that name already exist";

    public NameTakenException(){

    }

    public NameTakenException(String message) {
        super(message);
    }
}
