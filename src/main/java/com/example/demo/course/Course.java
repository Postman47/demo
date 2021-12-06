package com.example.demo.course;

import com.example.demo.student.Student;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Data
public class Course {
    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long cId;
    private String name;
    private Integer amountOfPoints;
    private Integer maxNumberOfStudents;
    private Boolean mandatory;
    @ManyToMany(mappedBy = "courses")
    private Set<Student> student;

    public Course() {
    }

    public Course(String name, Integer amountOfPoints, Integer maxNumberOfStudents, Boolean mandatory) {
        this.name = name;
        this.amountOfPoints = amountOfPoints;
        this.maxNumberOfStudents = maxNumberOfStudents;
        this.mandatory = mandatory;
    }
}