package com.example.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {
            Student piotr = new Student(
                    "Piotr",
                    "piotr789@gmail.com",
                    LocalDate.of(2000, Month.JANUARY, 4)
            );

            System.out.println(piotr.getAge());

            Student jan = new Student(
                    "Jan",
                    "jan789@gmail.com",
                    LocalDate.of(2002, Month.JANUARY, 12)
            );

            repository.saveAll(
                    List.of(piotr, jan)
            );
        };
    }
}
