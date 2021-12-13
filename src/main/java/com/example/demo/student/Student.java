package com.example.demo.student;

import com.example.demo.course.Course;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table
@Data
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
    private Long sId;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private Integer age;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "sId"),
            inverseJoinColumns = @JoinColumn(name = "cId")
    )
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();


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

}


