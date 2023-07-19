package com.parking.project.parkinglot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotDto {
    private Long id;
    private String parkingLotName;
    private String parkingLotAddress;
    private double latitude;
    private double longitude;
}
