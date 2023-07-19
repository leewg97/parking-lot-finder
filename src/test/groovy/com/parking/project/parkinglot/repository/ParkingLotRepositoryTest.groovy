package com.parking.project.parkinglot.repository

import com.parking.project.AbstractIntegrationContainerBaseTest
import com.parking.project.parkinglot.entity.ParkingLot
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

class ParkingLotRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private ParkingLotRepository parkingLotRepository

    def setup() {
        parkingLotRepository.deleteAll()
    }

    def "GymRepository save"() {
        given:
        String address = "서울특별시 송파구 올림픽로35다길"
        String name = "교통공원길"
        double latitude = 37.51
        double longitude = 127.10

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(address)
                .parkingLotName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = parkingLotRepository.save(parkingLot)

        then:
        result.getParkingLotAddress() == address
        result.getParkingLotName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "GymRepository saveAll"() {
        given:
        String address = "서울특별시 송파구 올림픽로35다길"
        String name = "교통공원길"
        double latitude = 37.51
        double longitude = 127.10

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(address)
                .parkingLotName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        parkingLotRepository.saveAll(Arrays.asList(parkingLot))
        def result = parkingLotRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "서울특별시 송파구 탄천동로"
        String name = "탄천 공영주차장"

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(address)
                .parkingLotName(name)
                .build()

        when:
        parkingLotRepository.save(parkingLot)
        def result = parkingLotRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }

}
