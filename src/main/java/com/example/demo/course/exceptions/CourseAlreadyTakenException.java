package com.example.demo.course.exceptions;

public class CourseAlreadyTakenException extends Exception{
    public static final String COURSE_ALREADY_TAKEN = "Student already signed for this course";

    public CourseAlreadyTakenException(){
    }

    public CourseAlreadyTakenException(String message) {
        super(message);
    }
}
