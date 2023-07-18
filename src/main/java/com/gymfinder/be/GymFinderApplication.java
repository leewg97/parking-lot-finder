package com.gymfinder.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GymFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymFinderApplication.class, args);
    }

}
