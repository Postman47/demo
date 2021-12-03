package com.example.demo.student;



import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

import static com.example.demo.student.StudentController.studentPath;

@RestController
@RequestMapping(path = studentPath)
public class StudentController {

    private final StudentService studentService;
    public static final String studentPath = "api/v1/student";

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

    @PutMapping
    public void signStudentForCourse(
            @RequestParam Long studentId,
            @RequestParam String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException{
        studentService.signStudentForCourse(studentId, courseName);
    }

    @GetMapping(path = "{studentId}")
    public ResponseEntity<List> getStudentCourses(@PathVariable Long studentId){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentCourses(studentId));
    }
}
