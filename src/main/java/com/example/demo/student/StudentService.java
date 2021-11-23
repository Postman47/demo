package com.example.demo.student;

import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) throws EmailTakenException {
        checkEmail(student);
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) throws StudentDoesNotExistException {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new StudentDoesNotExistException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) throws StudentDoesNotExistException, EmailTakenException {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(studentId));

        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
            checkEmail(student);
            student.setEmail(email);
        }
    }

    public void checkEmail(Student student) throws EmailTakenException {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new EmailTakenException();
        }
    }
}
