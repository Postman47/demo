package com.example.demo.student;

import com.example.demo.course.Course;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Set;


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
    private Set<Course> courses;

//    @ManyToMany
//    @JoinTable(
//            name = "student_course",
//            joinColumns = @JoinColumn(name = "sId"),
//            inverseJoinColumns = @JoinColumn(name = "cId")
//    )
//    @JsonIgnore
//    private Set<String> finishedCourses;



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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(sId, student.sId) && Objects.equals(name, student.name) && Objects.equals(email, student.email) && Objects.equals(dateOfBirth, student.dateOfBirth) && Objects.equals(age, student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sId, name, email, dateOfBirth, age);
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


