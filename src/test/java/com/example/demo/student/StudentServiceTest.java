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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddNewStudent() {
        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );

        //when
        underTest.addNewStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }


    @Test
    void canDeleteStudent() {
        //given
        given(studentRepository.existsById(1L)).willReturn(true);

        //when
        underTest.deleteStudent(1L);

        //then
        verify(studentRepository).deleteById(1L);


    }

    @Test
    void willThrowWhenStudentDoesNotExists(){

        //given
        Student student = new Student(
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        underTest.addNewStudent(student);
        given(studentRepository.existsById(any())).willReturn(false);
        //then
        assertThatThrownBy(() -> underTest.deleteStudent(student.getId())).hasMessageContaining(StudentDoesNotExistException.ERROR_THERE_IS_NO_STUDENT_WITH_ID + student.getId());
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void canUpdateStudent() {
        //given
        Student student = new Student(
                1L,
                "Hanna",
                "hanna23@gmail.com",
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        //when
        Long idTest = 1L;
        String nameTest = "Emilia";
        String emailTest = "emilia12@hmail.com";
        given(studentRepository.findById(any())).willReturn(Optional.of(student));
        underTest.updateStudent(idTest,nameTest,emailTest);

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
        given(studentRepository.findStudentByEmail(student.getEmail())).willReturn(Optional.of(student));
        //then
        assertThatThrownBy(() -> underTest.checkEmail(student)).hasMessageContaining(EmailTakenException.EMAIL_TAKEN_EXCEPTION);

    }

}