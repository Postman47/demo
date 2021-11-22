package com.example.demo.student.exceptions;

public class StudentDoesNotExistException extends Exception{
    public StudentDoesNotExistException(String errorMessage, Long studentId){
        super(errorMessage + studentId);
    }
}
