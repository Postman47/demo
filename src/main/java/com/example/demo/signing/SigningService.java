package com.example.demo.signing;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.TooManyStudentsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void signStudent(Long studentId , String courseName){

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
}
