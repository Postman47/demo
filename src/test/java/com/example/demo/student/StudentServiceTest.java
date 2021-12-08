package com.example.demo.student;

import com.example.demo.student.exceptions.EmailTakenException;
import com.example.demo.student.exceptions.StudentDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepositoryMock;
    private StudentService studentService;

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
    void addNewStudentTest() throws EmailTakenException{
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );

        //when
        studentService.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepositoryMock).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
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
        studentService.addStudent(student);
        given(studentRepositoryMock.existsById(any())).willReturn(false);
        //then
        assertThatThrownBy(() -> studentService.deleteStudent(student.getSId())).hasMessageContaining(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + student.getSId());
        verify(studentRepositoryMock, never()).deleteById(any());
    }

    @Test
    void updateStudentTest() throws EmailTakenException, StudentDoesNotExistException{
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        Long idTest = 1L;
        doReturn(Optional.of(student)).when(studentRepositoryMock).findById(idTest);
        String expectedName = "Emilia";
        String expectedEmail = "emilia12@hmail.com";
        studentService.updateStudent(idTest,expectedName,expectedEmail);

        //then
        assertThat(student.getEmail()).isEqualTo(expectedEmail);
        assertThat(student.getName()).isEqualTo(expectedName);

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
        given(studentRepositoryMock.findStudentByEmail(student.getEmail())).willReturn(Optional.of(student));
        //then
        assertThatThrownBy(() -> studentService.checkIfEmailTaken(student)).hasMessageContaining(EmailTakenException.EMAIL_TAKEN_EXCEPTION);

    }

}