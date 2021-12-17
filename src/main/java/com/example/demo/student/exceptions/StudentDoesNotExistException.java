package com.example.demo.student.exceptions;

public class StudentDoesNotExistException extends Exception{

    public static final String ERROR_THERE_IS_NO_STUDENT_WITH_ID = "Error there is no student with id ";

    public StudentDoesNotExistException() {
    }

    public StudentDoesNotExistException(String message) {
        super(message);
    }
}
