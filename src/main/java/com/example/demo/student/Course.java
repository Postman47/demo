//package com.example.demo.student;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table
//public class Course {
//    @Id
//    @SequenceGenerator(
//            name = "student_sequence",
//            sequenceName = "student_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "student_sequence"
//    )
//    @Getter
//    @Setter
//    private Long cId;
//
//    @ManyToMany
//    private Set<Student> student;
//}
