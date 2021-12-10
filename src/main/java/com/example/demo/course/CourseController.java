package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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
        return courseService.getCourses();
    }

    @PostMapping
    public ResponseEntity<String> registerCourse(@RequestBody Course course) throws NameTakenException {
        Course addCourse = new Course(course.getName(), course.getAmountOfPoints(), course.getMaxNumberOfStudents(), course.getMandatory());
        return courseService.addCourse(addCourse);
    }

    @DeleteMapping(path = "{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable("courseId") Long courseId) throws CourseDoesNotExistException {
        return courseService.deleteCourse(courseId);
    }

    @PutMapping(path = "{courseId}")
    public ResponseEntity<String> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer amountOfPoints,
            @RequestParam(required = false) Integer maxNumberOfStudents,
            @RequestParam(required = false) Boolean mandatory) throws CourseDoesNotExistException {
        return courseService.updateCourse(courseId, name, amountOfPoints, maxNumberOfStudents, mandatory);
    }
}
