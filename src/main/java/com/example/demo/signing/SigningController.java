package com.example.demo.signing;

import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.StudentNotSignedForcourseException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.Constant.*;

@RestController
@RequestMapping(path = signingPath)
public class SigningController {

    private final SigningService signingService;

    @Autowired
    public SigningController(SigningService signingService) {
        this.signingService = signingService;
    }

    @PutMapping
    public ResponseEntity<String> signStudent(
            @RequestParam Long studentId,
            @RequestParam String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException, CourseAlreadyTakenException, TooManyStudentsException {
        return signingService.signStudent(studentId, courseName);
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<String> resignStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException, StudentNotSignedForcourseException {
        return signingService.resignStudent(studentId, courseName);
    }
}
