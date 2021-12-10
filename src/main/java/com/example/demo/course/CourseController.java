package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.example.demo.Constant.*;

@RestController
@RequestMapping(path = coursePath)
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getCourse(){

        List<Course> response = courseService.getCourses();
        if (response.equals(null)) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, NO_CONTENT_EXCEPTION_MESSAGE);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourses());
        }
    }

    @PostMapping
    public ResponseEntity<String> registerCourse(@RequestBody Course course) throws NameTakenException {
        Course addCourse = new Course(course.getName(), course.getAmountOfPoints(), course.getMaxNumberOfStudents(), course.getMandatory());
        courseService.addNewCourse(addCourse);

        Optional<Course> courseOptional = courseService.getCourseRepository().findCourseByName(course.getName());
        if(courseOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_REGISTRATION_MESSAGE);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(REGISTERED_MESSAGE + addCourse.getName());
        }
    }

    @DeleteMapping(path = "{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable("courseId") Long courseId) throws CourseDoesNotExistException {
        courseService.deleteCourse(courseId);

        Optional<Course> optionalCourse = courseService.getCourseRepository().findById(courseId);
        if(optionalCourse.isPresent()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, FAILED_DELETION_MESSAGE + courseId);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(DELETED_MESSAGE + courseId);
        }
    }

    @PutMapping(path = "{courseId}")
    public ResponseEntity<String> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer amountOfPoints,
            @RequestParam(required = false) Integer maxNumberOfStudents,
            @RequestParam(required = false) Boolean mandatory) throws CourseDoesNotExistException{
        courseService.updateCourse(courseId, name, amountOfPoints, maxNumberOfStudents, mandatory);

        Optional<Course> optionalCourse = courseService.getCourseRepository().findById(courseId);
        if(!name.equals(null) || !amountOfPoints.equals(null) || !maxNumberOfStudents.equals(null) || !mandatory.equals(null)){
            if(optionalCourse.get().getName().equals(name) || optionalCourse.get().getAmountOfPoints().equals(amountOfPoints) || optionalCourse.get().getMaxNumberOfStudents().equals(maxNumberOfStudents) || optionalCourse.get().getMandatory().equals(mandatory)){
                return ResponseEntity.status(HttpStatus.OK).body(UPDATED_INSTANCE_WITH_ID_MESSAGE + courseId);
            }else {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ERROR_DID_NOT_UPDATE_INSTANCE_WITH_ID_MESSAGE);
            }
        }else{
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, NOT_ENOUGH_DATA_MESSAGE);
        }
    }


}
