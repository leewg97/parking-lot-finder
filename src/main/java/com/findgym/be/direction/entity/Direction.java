package com.findgym.be.direction.entity;

import com.findgym.be.gym.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    // 헬스장
    private String targetGymName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    // 사용자 주소와 헬스장 사이의 거리
    private double distance;
}
