package com.example.demo.course;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class CourseConfig {

    @Bean
    CommandLineRunner courseCommandLineRunner(CourseRepository repository){
        return args -> {
            Course analiza = new Course(
                    "Analiza Matematyczna 1",
                    4,
                    100,
                    true
            );

            Course algebra = new Course(
                    "Algebra",
                    5,
                    100,
                    true
            );

            repository.saveAll(
                    List.of(analiza, algebra)
            );
        };
    }
}
