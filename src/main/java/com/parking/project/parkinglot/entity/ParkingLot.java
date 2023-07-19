package com.parking.project.parkinglot.entity;

import com.parking.project.parkinglot.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "parking_lot")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parkingLotName;
    private String parkingLotAddress;
    private double latitude;
    private double longitude;

    public void changeParkingLotAddress(String address) {
        this.parkingLotAddress = address;
    }
}
