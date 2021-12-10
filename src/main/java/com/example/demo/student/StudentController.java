package com.example.demo.student;



import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

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

        return studentService.getStudents();
    }

    @PostMapping
    public ResponseEntity<String> registerStudent(@RequestBody Student student) throws EmailTakenException {
        Student addStudent = new Student(student.getName(),student.getEmail(),student.getDateOfBirth());
        return studentService.addStudent(addStudent);
    }

    @DeleteMapping(path = "{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long studentId) throws StudentDoesNotExistException {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) throws StudentDoesNotExistException, EmailTakenException{
                 return studentService.updateStudent(studentId, name, email);
    }


    @GetMapping(path = "{studentId}")
    public ResponseEntity<List<String>> getStudentCourses(@PathVariable Long studentId) throws StudentDoesNotExistException{
        return studentService.getStudentCourses(studentId);
    }
}
