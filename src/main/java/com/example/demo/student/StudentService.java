package com.example.demo.student;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.TooManyStudentsException;
import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        checkEmail(student);
        studentRepository.save(student);

    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));

        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)){
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
            checkEmail(student);
            student.setEmail(email);
        }
    }

    public void checkEmail(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new EmailTakenException(EmailTakenException.EMAIL_TAKEN_EXCEPTION);
        }
    }

    @Transactional
    public void signStudentForCourse(Long studentId , String courseName){

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));
        Course course = courseRepository.findCourseByName(courseName).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST));
        if(course.getStudent().size() >= course.getMaxNumberOfStudents()){
            throw new TooManyStudentsException(TooManyStudentsException.TOO_MANY_STUDENTS);
        }else if(!student.getCourses().contains(course)){
            student.getCourses().add(course);
        }else{
            throw new CourseAlreadyTakenException(CourseAlreadyTakenException.COURSE_ALREADY_TAKEN);
        }

    }

    public List<String> getStudentCourses(Long studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));

        List<String> response = new ArrayList<>();

        for(Course c: student.getCourses()){
            response.add(c.getName());
        }
        return response;
    }
}
