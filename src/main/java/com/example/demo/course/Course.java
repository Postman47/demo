package com.example.demo.course;

import com.example.demo.student.Student;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
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
    @Getter
    @Setter
    private Long cId;
    @Getter@Setter
    private String name;
    @Getter@Setter
    private Integer amountOfPoints;
    @Getter@Setter
    private Integer maxNumberOfStudents;
    @Getter@Setter
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
