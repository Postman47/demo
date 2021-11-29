package com.example.demo.student;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;


@Entity
@Table
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    @Getter @Setter
    private Long sId;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private LocalDate dateOfBirth;
    @Setter
    @GeneratedValue
    private Integer age;

//    @ManyToMany
//    @JoinTable(
//            name = "student_course"
//    )
//    private Set<Course> courses;


    public Student() {
    }

    public Student(String name,
                   String email,
                   LocalDate dateOfBirth) {
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

//    only for testing
    public Student(Long sId, String name,
                   String email,
                   LocalDate dateOfBirth) {
        this.sId = sId;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + sId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dateOfBirth +
                ", age=" + age +
                '}';
    }

    public Integer getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}


