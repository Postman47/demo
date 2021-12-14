package com.example.demo.signing;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.example.demo.Constant.FAILED_SIGNING_MESSAGE;
import static com.example.demo.Constant.SIGNED_MESSAGE;

@Service
@Data
public class SigningService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public SigningService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public ResponseEntity<String> signStudent(Long studentId , String courseName) throws CourseAlreadyTakenException, StudentDoesNotExistException, CourseDoesNotExistException, TooManyStudentsException{

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));
        Course course = courseRepository.findCourseByName(courseName).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST));
        if(course.getStudent().size() >= course.getMaxNumberOfStudents()){
            throw new TooManyStudentsException(TooManyStudentsException.TOO_MANY_STUDENTS);
        }else if(!student.getCourses().contains(course)){
            student.getCourses().add(course);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if(optionalStudent.get().getCourses().contains(courseRepository.findCourseByName(courseName))){
                return ResponseEntity.status(HttpStatus.OK).body(SIGNED_MESSAGE + courseName);
            }else{
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_SIGNING_MESSAGE + courseName);
            }
        }else{
            throw new CourseAlreadyTakenException(CourseAlreadyTakenException.COURSE_ALREADY_TAKEN);
        }

    }
}
