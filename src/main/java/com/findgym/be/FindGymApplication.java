package com.findgym.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FindGymApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindGymApplication.class, args);
    }

}
