package com.parking.project.direction.service

import com.parking.project.api.dto.DocumentDto
import com.parking.project.api.service.KakaoCategorySearchService
import com.parking.project.direction.repository.DirectionRepository
import com.parking.project.parkinglot.dto.ParkingLotDto
import com.parking.project.parkinglot.service.ParkingLotSearchService
import spock.lang.Specification

class DirectionServiceTest extends Specification {

    private ParkingLotSearchService gymSearchService = Mock()
    private DirectionRepository directionRepository = Mock()
    private KakaoCategorySearchService kakaoCategorySearchService = Mock()
    private Base62Service base62Service = Mock()

    private DirectionService directionService = new DirectionService(
            gymSearchService, directionRepository, kakaoCategorySearchService, base62Service)

    private List<ParkingLotDto> gymList

    def setup() {
        gymList = new ArrayList<>()
        gymList.addAll(
                ParkingLotDto.builder()
                        .id(1L)
                        .parkingLotName("교통공원길")
                        .parkingLotAddress("서울특별시 송파구 올림픽로35다길")
                        .latitude(37.5150312)
                        .longitude(127.101145)
                        .build(),
                ParkingLotDto.builder()
                        .id(2L)
                        .parkingLotName("아시아")
                        .parkingLotAddress("서울특별시 송파구 올림픽로44")
                        .latitude(37.5095827)
                        .longitude(127.0760964)
                        .build()
        )
    }

    def "buildDirectionList - 거리순으로 결과 값이 정렬 되는지 확인"() {
        given:
        def addressName = "서울특별시 송파구 올림픽로35다길"
        double inputLatitude = 37.5150312
        double inputLongitude = 127.101145

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        gymSearchService.searchParkingLotDtoList() >> gymList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetParkingLotName == "교통공원길"
        results.get(1).targetParkingLotName == "아시아"
    }

    def "buildDirectionList - 정해진 반경 10KM 내에 검색이 되는지 확인"() {
        given:
        gymList.add(
                ParkingLotDto.builder()
                        .id(3L)
                        .parkingLotName("주차장")
                        .parkingLotAddress("주소")
                        .latitude(37.3825107393401)
                        .longitude(127.236707811313)
                        .build()
        )

        def addressName = "서울특별시 송파구 올림픽로35다길"
        double inputLatitude = 37.5150312
        double inputLongitude = 127.101145

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        gymSearchService.searchParkingLotDtoList() >> gymList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetParkingLotName == "교통공원길"
        results.get(1).targetParkingLotName == "아시아"
    }
}
