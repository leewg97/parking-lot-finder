package com.parking.project.parkinglot.cache

import com.parking.project.AbstractIntegrationContainerBaseTest
import com.parking.project.parkinglot.dto.ParkingLotDto
import org.springframework.beans.factory.annotation.Autowired

class ParkingLotRedisTemplateTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private ParkingLotRedisTemplate parkingLotRedisTemplate

    def setup() {
        parkingLotRedisTemplate.findAll()
                .forEach(dto -> {
                    parkingLotRedisTemplate.delete(dto.getId())
                })
    }

    def "save success"() {
        given:
        String parkingLotName = "name"
        String parkingLotAddress = "address"
        ParkingLotDto dto =
                ParkingLotDto.builder()
                        .id(1L)
                        .parkingLotName(parkingLotName)
                        .parkingLotAddress(parkingLotAddress)
                        .build()

        when:
        parkingLotRedisTemplate.save(dto)
        List<ParkingLotDto> result = parkingLotRedisTemplate.findAll()

        then:
        result.size() == 1
        result.get(0).id == 1L
        result.get(0).parkingLotName == parkingLotName
        result.get(0).parkingLotAddress == parkingLotAddress
    }

    def "success fail"() {
        given:
        ParkingLotDto dto =
                ParkingLotDto.builder()
                        .build()

        when:
        parkingLotRedisTemplate.save(dto)
        List<ParkingLotDto> result = parkingLotRedisTemplate.findAll()

        then:
        result.size() == 0
    }

    def "delete"() {
        given:
        String parkingLotName = "name"
        String parkingLotAddress = "address"
        ParkingLotDto dto =
                ParkingLotDto.builder()
                        .id(1L)
                        .parkingLotName(parkingLotName)
                        .parkingLotAddress(parkingLotAddress)
                        .build()

        when:
        parkingLotRedisTemplate.save(dto)
        parkingLotRedisTemplate.delete(dto.getId())
        def result = parkingLotRedisTemplate.findAll()

        then:
        result.size() == 0
    }
}
