package com.parking.project.parkinglot.controller;

import com.parking.project.parkinglot.cache.ParkingLotRedisTemplate;
import com.parking.project.parkinglot.dto.ParkingLotDto;
import com.parking.project.parkinglot.service.ParkingLotRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotRepositoryService parkingLotRepositoryService;
    private final ParkingLotRedisTemplate parkingLotRedisTemplate;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {

        List<ParkingLotDto> parkingLotDtoList = parkingLotRepositoryService.findAll()
                .stream().map(parkinglot -> ParkingLotDto.builder()
                        .id(parkinglot.getId())
                        .parkingLotName(parkinglot.getParkingLotName())
                        .parkingLotAddress(parkinglot.getParkingLotAddress())
                        .latitude(parkinglot.getLatitude())
                        .longitude(parkinglot.getLongitude())
                        .build())
                .collect(Collectors.toList());

        parkingLotDtoList.forEach(parkingLotRedisTemplate::save);

        return "success";
    }
}
