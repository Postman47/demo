package com.example.demo.student;



import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.demo.student.StudentController.apiPath;

@RestController
@RequestMapping(path = apiPath)
public class StudentController {

    private final StudentService studentService;
    public static final String apiPath = "api/v1/student";

    @Autowired
    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List> getStudent(){

        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudents());

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


}
