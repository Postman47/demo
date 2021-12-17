package com.example.demo.signing;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.StudentNotSignedForcourseException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.exceptions.CourseAlreadyFinishedException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.example.demo.Constant.*;

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
    public ResponseEntity<String> signStudent(Long studentId , String courseName) throws CourseAlreadyTakenException, StudentDoesNotExistException, CourseDoesNotExistException, TooManyStudentsException, CourseAlreadyFinishedException {

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));
        Course course = courseRepository.findCourseByName(courseName).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST));
 //       checkIfFinishedCourse(student, course);
        if(course.getStudent().size() >= course.getMaxNumberOfStudents()){
            throw new TooManyStudentsException(TooManyStudentsException.TOO_MANY_STUDENTS);
        }else if(!student.getCourses().contains(course)){
            student.getCourses().add(course);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            Optional<Course> optionalCourse = courseRepository.findCourseByName(courseName);
            if(optionalStudent.get().getCourses().contains(optionalCourse.get())){
                return ResponseEntity.status(HttpStatus.OK).body(SIGNED_MESSAGE + courseName);
            }else{
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_SIGNING_MESSAGE + courseName);
            }
        }else{
            throw new CourseAlreadyTakenException(CourseAlreadyTakenException.COURSE_ALREADY_TAKEN);
        }
    }

    @Transactional
    public ResponseEntity<String> resignStudent(Long studentId, String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException, StudentNotSignedForcourseException {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));
        Course course = courseRepository.findCourseByName(courseName).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST));
        checkIfNotSigned(student, course);
        student.getCourses().remove(course);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if(optionalStudent.get().getCourses().contains(course)){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_RESIGNING_MESSAGE + courseName);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(student.getName() + RESIGNED_MESSAGE + course.getName());
        }

    }

//    @Transactional
//    public ResponseEntity<String> finishCourse(Long studentId, String courseName) throws StudentDoesNotExistException, CourseDoesNotExistException, CourseAlreadyFinishedException, StudentNotSignedForcourseException {
//        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentDoesNotExistException(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + studentId));
//        Course course = courseRepository.findCourseByName(courseName).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST));
//        checkIfNotSigned(student, course);
//        checkIfFinishedCourse(student, course);
//        student.getCourses().remove(course);
//        student.getFinishedCourses().add(course);
//        Optional<Student> optionalStudent = studentRepository.findById(studentId);
//        Optional<Course> optionalCourse = courseRepository.findCourseByName(courseName);
//        if(optionalStudent.get().getFinishedCourses().contains(optionalCourse.get()) && !optionalStudent.get().getCourses().contains(optionalCourse.get())){
//            return ResponseEntity.status(HttpStatus.OK).body(student.getName() + FINISHED_COURSE_MESSAGE + courseName);
//        }else {
//            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_FINISHING_COURSE_MESSAGE);
//        }
//
//    }
//
//    public void checkIfFinishedCourse(Student student, Course course) throws CourseAlreadyFinishedException {
//        if(student.getFinishedCourses().contains(course)){
//            throw new CourseAlreadyFinishedException(CourseAlreadyFinishedException.COURSE_ALREADY_FINISHED);
//        }
//    }

    public void checkIfNotSigned(Student student, Course course) throws StudentNotSignedForcourseException {
        if(!student.getCourses().contains(course)){
            throw new StudentNotSignedForcourseException(StudentNotSignedForcourseException.NOT_SIGNED + student.getName());
        }
    }
}
