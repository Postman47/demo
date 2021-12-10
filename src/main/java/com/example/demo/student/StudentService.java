package com.example.demo.student;

import com.example.demo.course.Course;
import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static com.example.demo.Constant.*;

@Service
@Data
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<List<Student>> getStudents(){
        if (studentRepository.findAll().equals(null)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, NO_CONTENT_EXCEPTION_MESSAGE);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(studentRepository.findAll());
        }
    }

    public ResponseEntity<String> addStudent(Student student) throws EmailTakenException{
        checkIfEmailTaken(student);
        studentRepository.save(student);

        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_REGISTRATION_MESSAGE);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(REGISTERED_MESSAGE + student.getName());
        }

    }

    public ResponseEntity<String> deleteStudent(Long studentId) throws StudentDoesNotExistException{
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId);
        }
        studentRepository.deleteById(studentId);

        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if(optionalStudent.isPresent()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_DELETION_MESSAGE + studentId);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(DELETED_MESSAGE + studentId);
        }
    }

    @Transactional
    public ResponseEntity<String> updateStudent(Long studentId, String name, String email) throws EmailTakenException, StudentDoesNotExistException {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));

        if(name != null && name.length() > 0 && !student.getName().equals(name)){
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !student.getEmail().equals(email)){
            checkIfEmailTaken(student);
            student.setEmail(email);
        }

        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if(!name.equals(null) || !email.equals(null)){
            if(optionalStudent.get().getName().equals(name) || optionalStudent.get().getEmail().equals(email)){
                return ResponseEntity.status(HttpStatus.OK).body(UPDATED_INSTANCE_WITH_ID_MESSAGE + studentId);
            }else {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ERROR_DID_NOT_UPDATE_INSTANCE_WITH_ID_MESSAGE);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, NOT_ENOUGH_DATA_MESSAGE);
        }
    }

    public void checkIfEmailTaken(Student student) throws EmailTakenException{
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new EmailTakenException(EmailTakenException.EMAIL_TAKEN_EXCEPTION);
        }
    }

    public ResponseEntity<List<String>> getStudentCourses(Long studentId) throws StudentDoesNotExistException{
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));

        List<String> response = new ArrayList<>();

        for(Course c: student.getCourses()){
            response.add(c.getName());
        }
        if(response.equals(null)){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, NO_CONTENT_EXCEPTION_MESSAGE);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
