package com.example.demo.student;



import com.example.demo.course.Course;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

import static com.example.demo.Constant.*;


@RestController
@RequestMapping(path = studentPath)
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudent(){

        List<Student> response = studentService.getStudents();
        if (response.equals(null)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, NO_CONTENT_EXCEPTION_MESSAGE);
        }else if(response != studentService.getStudents()){
            throw new ResponseStatusException(HttpStatus.PARTIAL_CONTENT, PARTIAL_CONTENT_EXCEPTION_MESSAGE);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());
        }

    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        Student addStudent = new Student(student.getName(),student.getEmail(),student.getDateOfBirth());
        studentService.addNewStudent(addStudent);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) throws StudentDoesNotExistException {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) throws StudentDoesNotExistException{
                studentService.updateStudent(studentId, name, email);
    }

    @PutMapping
    public void signStudentForCourse(
            @RequestParam Long studentId,
            @RequestParam String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException{
        studentService.signStudentForCourse(studentId, courseName);
    }

    @GetMapping(path = "{studentId}")
    public ResponseEntity<List<String>> getStudentCourses(@PathVariable Long studentId){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentCourses(studentId));
    }
}
