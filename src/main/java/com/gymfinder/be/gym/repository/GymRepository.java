package com.gymfinder.be.gym.repository;

import com.gymfinder.be.gym.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
