package com.example.demo.course.exceptions;

public class CourseDoesNotExistException extends Exception{
    public static final String COURSE_DO_NOT_EXIST = "Course with that id does not exist";

    public CourseDoesNotExistException(){

    }

    public CourseDoesNotExistException(String message) {
        super(message);
    }
}
