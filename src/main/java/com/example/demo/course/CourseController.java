package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.Constant.coursePath;

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
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourses());
    }

    @PostMapping
    public void registerCourse(@RequestBody Course course) throws NameTakenException {
        Course addCourse = new Course(course.getName(), course.getAmountOfPoints(), course.getMaxNumberOfStudents(), course.getMandatory());
        courseService.addNewCourse(addCourse);
    }

    @DeleteMapping(path = "{courseId}")
    public void deleteCourse(@PathVariable("courseId") Long courseId) throws CourseDoesNotExistException {
        courseService.deleteCourse(courseId);
    }

    @PutMapping(path = "{courseId}")
    public void updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer amountOfPoints,
            @RequestParam(required = false) Integer maxNumberOfStudents,
            @RequestParam(required = false) Boolean mandatory) throws CourseDoesNotExistException{
        courseService.updateCourse(courseId, name, amountOfPoints, maxNumberOfStudents, mandatory);
    }


}
