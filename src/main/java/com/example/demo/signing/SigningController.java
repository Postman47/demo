package com.example.demo.signing;

import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class SigningController {

    private final SigningService signingService;

    @Autowired
    public SigningController(SigningService signingService) {
        this.signingService = signingService;
    }

    @PutMapping
    public void signStudent(
            @RequestParam Long studentId,
            @RequestParam String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException, CourseAlreadyTakenException, TooManyStudentsException {
        signingService.signStudent(studentId, courseName);
    }
}
