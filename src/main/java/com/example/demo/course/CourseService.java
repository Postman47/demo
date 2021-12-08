package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;

import com.example.demo.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses(){
        return courseRepository.findAll();
    }

    public void addNewCourse(Course course) {
        checkIfNameTaken(course);
        courseRepository.save(course);

    }

    public void deleteCourse(Long studentId) {
        boolean exists = courseRepository.existsById(studentId);
        if(!exists){
            throw new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST + studentId);
        }
        courseRepository.deleteById(studentId);
    }

    @Transactional
    public void updateCourse(Long courseId,String name, Integer amountOfPoints, Integer maxNumberOfStudents, Boolean mandatory) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST + courseId));

        if(name != null && name.length() > 0 && !course.getName().equals(name)){
            course.setName(name);
        }

        if(amountOfPoints != null && amountOfPoints > 0 && !course.getAmountOfPoints().equals(amountOfPoints)){
            course.setAmountOfPoints(amountOfPoints);
        }

        if(amountOfPoints != null && maxNumberOfStudents > 0 && !course.getMaxNumberOfStudents().equals(maxNumberOfStudents)){
            course.setMaxNumberOfStudents(maxNumberOfStudents);
        }

        course.setMandatory(mandatory);

    }



    public void checkIfNameTaken(Course course) {
        Optional<Course> courseOptional = courseRepository.findCourseByName(course.getName());
        if (courseOptional.isPresent()){
            throw new NameTakenException(NameTakenException.NAME_TAKEN_EXCEPTION);
        }
    }
}
