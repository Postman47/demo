package com.example.demo.signing;

import com.example.demo.course.Course;
import com.example.demo.course.CourseRepository;
import com.example.demo.course.CourseService;
import com.example.demo.course.exceptions.CourseAlreadyTakenException;
import com.example.demo.course.exceptions.CourseDoesNotExistException;
import com.example.demo.signing.exceptions.TooManyStudentsException;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.StudentService;
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
    private List<Course> courseSetMock;


    @BeforeEach
    void setUp() {

        signingService = new SigningService(studentRepositoryMock, courseRepositoryMock);
    }

    @Test
    void signStudentTest() throws StudentDoesNotExistException, CourseAlreadyTakenException, CourseDoesNotExistException, TooManyStudentsException {
        // TODO: Make this test work
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
        Course course1 = new Course(
                "Algebra",
                4,
                20,
                true
        );
        //when
        Set<Student> studentSet = new HashSet<>();
        studentSet.add(student1);
        studentSet.add(student);
        course.setStudent(studentSet);
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(any());
        doReturn(Optional.of(course)).when(courseRepositoryMock).findCourseByName(course.getName());
        doReturn(Optional.of(course1)).when(courseRepositoryMock).findCourseByName(course1.getName());
//        doReturn(studentSetMock).when(courseMock).getStudent();
//        doReturn(25).when(courseMock).getMaxNumberOfStudents();
//        student.getCourses().add(course);
//        doReturn(courseSetMock).when(studentMock).getCourses();
        //doReturn(false).when(courseSetMock).contains(any());
        //then
        signingService.signStudent(1L, course.getName());
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
}
