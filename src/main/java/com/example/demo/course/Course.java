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
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    @Getter
    @Setter
    private Long cId;
    @Getter@Setter
    private String name;
    @Getter@Setter
    private Integer amountOfPoints;
    @Getter@Setter
    private Boolean mandatory;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> student;

    public Course(String name, Integer amountOfPoints, Boolean mandatory) {
        this.name = name;
        this.amountOfPoints = amountOfPoints;
        this.mandatory = mandatory;
    }
}
