package com.example.demo.student.exceptions;

public class StudentDoesNotExistException extends Exception{

    public static final String errorStudentWithIdDoesNotExist = "Error there is no student with id ";

    public StudentDoesNotExistException(Long studentId){
        super(errorStudentWithIdDoesNotExist + studentId);
    }
}
