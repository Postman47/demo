package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail() {
        //given
        String email = "hanna23@gmail.com";
        Student student = new Student(
                "Hanna",
                email,
                LocalDate.of(1993, Month.DECEMBER, 17)
        );
        underTest.save(student);

        //when
        boolean expected = underTest.findStudentByEmail(email).isPresent();

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldNotFindStudentBecauseEmailDoNotExists() {
        //given
        String email = "hanna23@gmail.com";

        //when
        boolean expected = underTest.findStudentByEmail(email).isPresent();

        //then
        assertThat(expected).isFalse();
    }
}