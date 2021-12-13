package com.example.demo.student;

import com.example.demo.course.Course;
import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;

import java.util.Optional;

import static com.example.demo.Constant.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepositoryMock;
    private StudentService studentService;
    @Mock
    private Student studentMock;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepositoryMock);
    }

    @Test
    void getStudentsTest() {
        //when
        studentService.getStudents();
        //then
        verify(studentRepositoryMock).findAll();
    }

    @Test
    void throwWhenNoContent_getStudents(){

        //given
        doReturn(null).when(studentRepositoryMock).findAll();

        //when
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            studentService.getStudents();
        });

        String expectedMessage = NO_CONTENT_EXCEPTION_MESSAGE;
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addStudentTest() throws EmailTakenException{
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );

        //when
        doReturn(Optional.of(student)).when(studentRepositoryMock).findStudentByEmail(student.getEmail());
        studentService.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepositoryMock).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void throwWhenFailedRegistration_addStudent(){

        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        doReturn(null).when(studentRepositoryMock).findStudentByEmail(student.getEmail());
        //when
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            studentService.addStudent(student);
        });

        String expectedMessage = FAILED_REGISTRATION_MESSAGE;
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteStudentTest() throws StudentDoesNotExistException{
        //given
        given(studentRepositoryMock.existsById(1L)).willReturn(true);

        //when
        studentService.deleteStudent(1L);

        //then
        verify(studentRepositoryMock).deleteById(1L);


    }

    @Test
    void willThrowWhenStudentDoesNotExists() throws EmailTakenException{

        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        given(studentRepositoryMock.existsById(any())).willReturn(false);
        //then
        assertThatThrownBy(() -> studentService.deleteStudent(student.getSId())).hasMessageContaining(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + student.getSId());
        verify(studentRepositoryMock, never()).deleteById(any());
    }

    @Test
    void throwWhenFailedExpectations_deleteStudent(){
        //given
        Long id = 1L;
        //when
        doReturn(true).when(studentRepositoryMock).existsById(id);
        doReturn(null).when(studentRepositoryMock).findById(id);

        //then
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            studentService.deleteStudent(id);
        });

        String expectedMessage = FAILED_DELETION_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateStudentTest() throws EmailTakenException, StudentDoesNotExistException{
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        Long idTest = 1L;
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(idTest);

        //when
        String expectedName = "Emilia";
        String expectedEmail = "emilia12@hmail.com";
        studentService.updateStudent(idTest,expectedName,expectedEmail);

        //then
        assertThat(student.getEmail()).isEqualTo(expectedEmail);
        assertThat(student.getName()).isEqualTo(expectedName);

    }

    @Test
    void throwWhenDidNotUpdateStudent_updateStudent(){
        //given
        Long idTest = 1L;
        String name = "Miroslaw";
        String email = "miroslaw256@gmail.com";

        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(idTest);
        doReturn("Hanna").when(studentMock).getName();
        doReturn("hanna20954@gmail.com").when(studentMock).getEmail();


        //then
        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            studentService.updateStudent(idTest, name, email);
        });

        String expectedMessage = ERROR_DID_NOT_UPDATE_INSTANCE_WITH_ID_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void throwWhenNotEnoughData_updateStudent(){
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        Long idTest = 1L;
        String name = null;
        String email = null;
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(idTest);


        Exception exception = assertThrows(ResponseStatusException.class, () ->{
            studentService.updateStudent(idTest, name, email);
        });

        String expectedMessage = NOT_ENOUGH_DATA_MESSAGE;
        String actualMessage = exception.getMessage();


        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void willThrowWhenEmailIsTaken(){
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        doReturn(true).when(studentRepositoryMock).existsByEmail(student.getEmail());

        //then
        Exception exception = assertThrows(Exception.class, () ->{
            studentService.checkIfEmailTaken(student);
        });

        String expectedMessage = EmailTakenException.EMAIL_TAKEN_EXCEPTION;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getStudentCoursesTest() throws StudentDoesNotExistException{
        Course course = new Course(
                "Fizyka",
                4,
                20,
                true
        );
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        student.getCourses().add(course);
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(any());
        ResponseEntity responseEntity = studentService.getStudentCourses(1L);

        assertTrue(responseEntity.getBody().toString().contains(course.getName()));

    }

    @Test
    void throwWhenNoData_getStudentCoursesTest(){
        //when
        doReturn(Optional.of(studentMock)).when(studentRepositoryMock).findById(any());
        //then
        Exception exception = assertThrows(Exception.class, () ->{
            studentService.getStudentCourses(1L);
        });

        String expectedMessage = NO_CONTENT_EXCEPTION_MESSAGE;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



}