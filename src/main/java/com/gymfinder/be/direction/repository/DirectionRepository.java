package com.gymfinder.be.direction.repository;

import com.gymfinder.be.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
