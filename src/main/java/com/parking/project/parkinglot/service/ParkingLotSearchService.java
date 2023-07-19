package com.parking.project.parkinglot.service;

import com.parking.project.parkinglot.dto.ParkingLotDto;
import com.parking.project.parkinglot.entity.ParkingLot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingLotSearchService {

    private final ParkingLotRepositoryService parkingLotRepositoryService;

    public List<ParkingLotDto> searchParkingLotDtoList() {
        // TODO: redis

        // db
        return parkingLotRepositoryService.findAll()
                .stream()
                .map(this::convertToParkingLotDto)
                .collect(Collectors.toList());
    }

    private ParkingLotDto convertToParkingLotDto(ParkingLot parkingLot) {
        return ParkingLotDto.builder()
                .id(parkingLot.getId())
                .parkingLotAddress(parkingLot.getParkingLotAddress())
                .parkingLotName(parkingLot.getParkingLotName())
                .latitude(parkingLot.getLatitude())
                .longitude(parkingLot.getLongitude())
                .build();
    }

}
