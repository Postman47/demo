package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo.Constant.*;

@Service
@Data
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public ResponseEntity<List<Course>> getCourses(){
        List<Course> response = courseRepository.findAll();
        if (Objects.isNull(response)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, NO_CONTENT_EXCEPTION_MESSAGE);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    public ResponseEntity<String> addCourse(Course course) throws NameTakenException{
        checkIfNameTaken(course);
        courseRepository.save(course);

        Optional<Course> courseOptional = courseRepository.findCourseByName(course.getName());
        if(Objects.isNull(courseOptional)){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_REGISTRATION_MESSAGE);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(REGISTERED_MESSAGE + course.getName());
        }

    }

    public ResponseEntity<String> deleteCourse(Long courseId) throws CourseDoesNotExistException{
        boolean exists = courseRepository.existsById(courseId);
        if(!exists){
            throw new CourseDoesNotExistException(CourseDoesNotExistException.COURSE_DO_NOT_EXIST + courseId);
        }
        courseRepository.deleteById(courseId);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(Objects.isNull(optionalCourse)){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_DELETION_MESSAGE + courseId);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(DELETED_MESSAGE + courseId);
        }
    }

    @Transactional
    public ResponseEntity<String> updateCourse(Long courseId, String name, Integer amountOfPoints, Integer maxNumberOfStudents, Boolean mandatory) throws CourseDoesNotExistException{
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

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!Objects.isNull(name) || !Objects.isNull(amountOfPoints) || !Objects.isNull(maxNumberOfStudents) || !Objects.isNull(mandatory)){
            if(optionalCourse.get().getName().equals(name) || optionalCourse.get().getAmountOfPoints().equals(amountOfPoints) || optionalCourse.get().getMaxNumberOfStudents().equals(maxNumberOfStudents) || optionalCourse.get().getMandatory().equals(mandatory)){
                return ResponseEntity.status(HttpStatus.OK).body(UPDATED_INSTANCE_WITH_ID_MESSAGE + courseId);
            }else {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ERROR_DID_NOT_UPDATE_INSTANCE_WITH_ID_MESSAGE);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, NOT_ENOUGH_DATA_MESSAGE);
        }

    }



    public void checkIfNameTaken(Course course) throws NameTakenException{
        if (courseRepository.existsByName(course.getName())){
            throw new NameTakenException(NameTakenException.NAME_TAKEN_EXCEPTION);
        }
    }
}
