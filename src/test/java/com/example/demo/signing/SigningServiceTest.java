package com.example.demo.signing;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.CourseService;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.StudentNotSignedForcourseException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;
import com.example.demo.student.exceptions.CourseAlreadyFinishedException;
import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.demo.Constant.FAILED_RESIGNING_MESSAGE;
import static com.example.demo.Constant.FAILED_SIGNING_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SigningServiceTest {

    @Mock
    private StudentRepository studentRepositoryMock;
    @Mock
    private CourseRepository courseRepositoryMock;
    private SigningService signingService;
    @Mock
    private Course courseMock;
    @Mock
    private Student studentMock;
    @Mock
    private Set<Student> studentSetMock;
    @Mock
    private Set<Course> courseSetMock;


    @BeforeEach
    void setUp() {

        signingService = new SigningService(studentRepositoryMock, courseRepositoryMock);
    }

    @Test
    void signStudentTest() throws StudentDoesNotExistException, CourseAlreadyTakenException, CourseDoesNotExistException, TooManyStudentsException, CourseAlreadyFinishedException {
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        Student student1 = new Student(
                "Maria",
                "maria23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );

        //when
        Set<Student> studentSet = new HashSet<>();
        Set<Course> courseSet = new HashSet<>();
        studentSet.add(student);
        courseSet.add(course);
        course.setStudent(studentSet);
        student.setCourses(courseSet);
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(course)).when(courseRepositoryMock).findCourseByName(course.getName());

        //then
        signingService.signStudent(1L, course.getName());

        assertTrue(student.getCourses().contains(course));
    }

    @Test
    void throwWhenTooManyStudents_signStudentTest(){
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findCourseByName(any());
        doReturn(5).when(courseMock).getMaxNumberOfStudents();
        doReturn(studentSetMock).when(courseMock).getStudent();
        doReturn(25).when(studentSetMock).size();

        Exception exception = assertThrows(Exception.class, () ->{
            signingService.signStudent(1L, "Fizyka");
        });

        String expectedMessage = TooManyStudentsException.TOO_MANY_STUDENTS;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenSigningFailure_signStudentTest(){

        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findCourseByName(any());
        doReturn(25).when(courseMock).getMaxNumberOfStudents();
        doReturn(studentSetMock).when(courseMock).getStudent();
        doReturn(5).when(studentSetMock).size();
        doReturn(courseSetMock).when(studentMock).getCourses();
        doReturn(false).when(courseSetMock).contains(any());

        Exception exception = assertThrows(Exception.class, () ->{
            signingService.signStudent(1L, "Fizyka");
        });

        String expectedMessage = FAILED_SIGNING_MESSAGE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenCourseAlreadyTaken_signStudentTest(){
        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findCourseByName(any());
        doReturn(25).when(courseMock).getMaxNumberOfStudents();
        doReturn(studentSetMock).when(courseMock).getStudent();
        doReturn(5).when(studentSetMock).size();
        doReturn(courseSetMock).when(studentMock).getCourses();
        doReturn(true).when(courseSetMock).contains(any());

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            signingService.signStudent(1L, "Fizyka");
        });

        String expectedMessage = CourseAlreadyTakenException.COURSE_ALREADY_TAKEN;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void resignStudentTest() throws StudentDoesNotExistException, CourseDoesNotExistException, StudentNotSignedForcourseException {
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );

        //when
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        student.setCourses(courseSet);
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(course)).when(courseRepositoryMock).findCourseByName(course.getName());

        //then
        signingService.resignStudent(1L, course.getName());

        assertTrue(!student.getCourses().contains(course));
    }

    @Test
    void throwWhenStudentNotSigned_resignStudentTest(){
        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findCourseByName(any());
        doReturn(courseSetMock).when(studentMock).getCourses();
        doReturn(false).when(courseSetMock).contains(any());

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            signingService.resignStudent(1L, "Fizyka");
        });

        String expectedMessage = StudentNotSignedForcourseException.NOT_SIGNED;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenDidNotResign_resignStudentTest(){
        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(courseMock)).when(courseRepositoryMock).findCourseByName(any());
        doReturn(courseSetMock).when(studentMock).getCourses();
        doReturn(true).when(courseSetMock).contains(any());

        //then
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            signingService.resignStudent(1L, "Fizyka");
        });

        String expectedMessage = FAILED_RESIGNING_MESSAGE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
