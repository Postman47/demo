package com.example.demo.course;

import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.course.exceptions.NameTakenException;
import com.example.demo.student.exceptions.EmailTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

import static com.example.demo.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepositoryMock;
    private CourseService courseService;
    @Mock
    private Course courseMock;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepositoryMock);
    }

    @Test
    void getCoursesTest() {
        //when
        courseService.getCourses();
        //then
        verify(courseRepositoryMock).findAll();
    }

    @Test
    void throwWhenNoContent_getCourses(){

        //given
        doReturn(null).when(courseRepositoryMock).findAll();

        //when
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            courseService.getCourses();
        });

        String expectedMessage = NO_CONTENT_EXCEPTION_MESSAGE;
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addCourseTest() throws NameTakenException {
        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );

        //when
        doReturn(Optional.of(course)).when(courseRepositoryMock).findCourseByName(course.getName());
        courseService.addCourse(course);

        //then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepositoryMock).save(courseArgumentCaptor.capture());
        Course capturedCourse = courseArgumentCaptor.getValue();
        assertThat(capturedCourse).isEqualTo(course);
    }

    @Test
    void throwWhenFailedRegistration_addCourse(){

        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        doReturn(null).when(courseRepositoryMock).findCourseByName(course.getName());
        //when
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            courseService.addCourse(course);
        });

        String expectedMessage = FAILED_REGISTRATION_MESSAGE;
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteCourseTest() throws CourseDoesNotExistException {
        //given
        given(courseRepositoryMock.existsById(1L)).willReturn(true);

        //when
        courseService.deleteCourse(1L);

        //then
        verify(courseRepositoryMock).deleteById(1L);


    }

    @Test
    void willThrowWhenCourseDoesNotExists() throws EmailTakenException{

        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        //when
        given(courseRepositoryMock.existsById(any())).willReturn(false);
        //then
        Exception exception = assertThrows(Exception.class, () ->{
            courseService.deleteCourse(1L);
        });

        String expectedMessage = CourseDoesNotExistException.COURSE_DO_NOT_EXIST;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenFailedExpectations_deleteCourse(){
        //given
        Long id = 1L;
        //when
        doReturn(true).when(courseRepositoryMock).existsById(id);
        doReturn(null).when(courseRepositoryMock).findById(id);

        //then
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            courseService.deleteCourse(id);
        });

        String expectedMessage = FAILED_DELETION_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateCourseTest() throws CourseDoesNotExistException{
        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        Long idTest = 1L;
        doReturn(Optional.of(course)).when(courseRepositoryMock).findById(idTest);

        //when
        String expectedName = "Teoria_obwodow";
        Integer expectedAmountOfPoints = 5;
        Integer expectedMaxNumberOfStudents = 25;
        Boolean expectedMandatory = true;
        courseService.updateCourse(idTest,expectedName,expectedAmountOfPoints,expectedMaxNumberOfStudents, expectedMandatory);

        //then
        assertThat(course.getName()).isEqualTo(expectedName);
        assertThat(course.getAmountOfPoints()).isEqualTo(expectedAmountOfPoints);
        assertThat(course.getMaxNumberOfStudents()).isEqualTo(expectedMaxNumberOfStudents);
        assertThat(course.getMandatory()).isEqualTo(expectedMandatory);


    }

    @Test
    void throwWhenDidNotUpdateCourse_updateCourse(){
        //given
        Long idTest = 1L;
        String expectedName = "Teoria_obwodow";
        Integer expectedAmountOfPoints = 5;
        Integer expectedMaxNumberOfStudents = 25;
        Boolean expectedMandatory = true;

        //when
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findById(idTest);
        doReturn("Fizyka").when(courseMock).getName();
        doReturn(3).when(courseMock).getAmountOfPoints();
        doReturn(3).when(courseMock).getMaxNumberOfStudents();
        doReturn(false).when(courseMock).getMandatory();


        //then
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            courseService.updateCourse(idTest, expectedName, expectedAmountOfPoints, expectedMaxNumberOfStudents, expectedMandatory);
        });

        String expectedMessage = ERROR_DID_NOT_UPDATE_INSTANCE_WITH_ID_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenNotEnoughData_updateCourse(){
        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        Long idTest = 1L;
        String name = null;
        Integer amountOfPoints = null;
        Integer maxNumberOfStudents = null;
        Boolean mandatory = null;
        doReturn(Optional.of(course)).when(courseRepositoryMock).findById(idTest);


        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            courseService.updateCourse(idTest, name, amountOfPoints, maxNumberOfStudents, mandatory);
        });

        String expectedMessage = NOT_ENOUGH_DATA_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void willThrowWhenNameIsTaken(){
        //given
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        //when
        doReturn(true).when(courseRepositoryMock).existsByName(course.getName());

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            courseService.checkIfNameTaken(course);
        });

        String expectedMessage = NameTakenException.NAME_TAKEN_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
