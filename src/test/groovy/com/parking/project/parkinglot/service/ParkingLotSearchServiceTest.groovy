package com.parking.project.parkinglot.service

import com.parking.project.parkinglot.cache.ParkingLotRedisTemplate
import com.parking.project.parkinglot.entity.ParkingLot
import spock.lang.Specification

class ParkingLotSearchServiceTest extends Specification {

    private ParkingLotSearchService parkingLotSearchService
    private ParkingLotRepositoryService parkingLotRepositoryService
    private ParkingLotRedisTemplate parkingLotRedisTemplate

    private List<ParkingLot> parkingLotList

    def setup() {
        parkingLotSearchService = new ParkingLotSearchService(parkingLotRepositoryService, parkingLotRedisTemplate)

        parkingLotList = Lists.newArrayList(
                ParkingLot.builder()
                        .id(1L)
                        .parkingLotName("교통공원길")
                        .latitude(37.5150312)
                        .longitude(127.101145)
                        .build(),
                ParkingLot.builder()
                        .id(2L)
                        .parkingLotName("아시아")
                        .latitude(37.5095827)
                        .longitude(127.0760964)
                        .build()
        )
    }

    def "레디스 장애시 DB를 이용하여 약국 데이터 조회"() {
        when:
        parkingLotRedisTemplate.findAll() >> []
        parkingLotRepositoryService.findAll() >> parkingLotList

        def result = parkingLotSearchService.searchParkingLotDtoList()

        then:
        result.size() == 2
    }
}
