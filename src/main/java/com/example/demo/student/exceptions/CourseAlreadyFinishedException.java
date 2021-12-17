package com.example.demo.student.exceptions;

public class CourseAlreadyFinishedException extends Exception{
    public static final String COURSE_ALREADY_FINISHED = " already finished ";

    public CourseAlreadyFinishedException() {
    }

    public CourseAlreadyFinishedException(String message) {
        super(message);
    }
}
