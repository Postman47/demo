package com.example.demo.student;



import com.example.demo.course.Course;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
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
    public ResponseEntity<String> registerNewStudent(@RequestBody Student student) throws EmailTakenException {
        Student addStudent = new Student(student.getName(),student.getEmail(),student.getDateOfBirth());
        studentService.addNewStudent(addStudent);

        Optional<Student> studentOptional = studentService.getStudentRepository().findStudentByEmail(student.getEmail());
        if(studentOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, STUDENT_NOT_REGISTERED_MESSAGE);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(NEW_STUDENT_HAS_BEEN_REGISTERED_MESSAGE + addStudent.getName());
        }
    }

    @DeleteMapping(path = "{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long studentId) throws StudentDoesNotExistException {
        studentService.deleteStudent(studentId);
        Optional<Student> optionalStudent = studentService.getStudentRepository().findById(studentId);
        if(optionalStudent.isPresent()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, STUDENT_HAS_NOT_BEEN_DELETED_MESSAGE + studentId);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(STUDENT_HAS_BEEN_DELETED_MESSAGE + studentId);
        }
    }

    @PutMapping(path = "{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) throws StudentDoesNotExistException, EmailTakenException{
                studentService.updateStudent(studentId, name, email);

                Optional<Student> optionalStudent = studentService.getStudentRepository().findById(studentId);
                if(optionalStudent.get().getName().equals(name) || optionalStudent.get().getEmail().equals(email)){
                    return ResponseEntity.status(HttpStatus.OK).body(STUDENT_HAS_BEEN_UPDATED + studentId);
                }else {
                    throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, STUDENT_HAS_NOT_BEEN_UPDATED);
                }
    }


    @GetMapping(path = "{studentId}")
    public ResponseEntity<List<String>> getStudentCourses(@PathVariable Long studentId) throws StudentDoesNotExistException{
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentCourses(studentId));
    }
}
