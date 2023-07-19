package com.parking.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ParkingLotFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingLotFinderApplication.class, args);
    }

}
