package com.gymfinder.be.direction.service

import com.gymfinder.be.api.dto.DocumentDto
import com.gymfinder.be.api.service.KakaoCategorySearchService
import com.gymfinder.be.direction.repository.DirectionRepository
import com.gymfinder.be.gym.dto.GymDto
import com.gymfinder.be.gym.service.GymSearchService
import spock.lang.Specification

class DirectionServiceTest extends Specification {

    private GymSearchService gymSearchService = Mock()
    private DirectionRepository directionRepository = Mock()
    private KakaoCategorySearchService kakaoCategorySearchService = Mock()
    private Base62Service base62Service = Mock()

    private DirectionService directionService = new DirectionService(
            gymSearchService, directionRepository, kakaoCategorySearchService, base62Service)

    private List<GymDto> gymList

    def setup() {
        gymList = new ArrayList<>()
        gymList.addAll(
                GymDto.builder()
                        .id(1L)
                        .gymName("에이블짐 신논현역")
                        .gymAddress("서울특별시 서초구 사평대로 371 (반포동)")
                        .latitude(37.5048336)
                        .longitude(127.0235227)
                        .build(),
                GymDto.builder()
                        .id(2L)
                        .gymName("에이블짐 교대역점")
                        .gymAddress("서울특별시 서초구 서초대로 320, 하림인터내셔날빌딩 지하2층 (서초동)")
                        .latitude(37.4940681)
                        .longitude(127.0161973)
                        .build()
        )
    }

    def "buildDirectionList - 거리순으로 결과 값이 정렬 되는지 확인"() {
        given:
        def addressName = "서울 서초구 서초대로 335"
        double inputLatitude = 37.4952387
        double inputLongitude = 127.0178924

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        gymSearchService.searchGymDtoList() >> gymList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetGymName == "에이블짐 교대역점"
        results.get(1).targetGymName == "에이블짐 신논현역"
    }

    def "buildDirectionList - 정해진 반경 10KM 내에 검색이 되는지 확인"() {
        given:
        gymList.add(
                GymDto.builder()
                        .id(3L)
                        .gymName("헬스장")
                        .gymAddress("주소")
                        .latitude(37.3825107393401)
                        .longitude(127.236707811313)
                        .build()
        )

        def addressName = "서울 서초구 서초대로 335"
        double inputLatitude = 37.4952387
        double inputLongitude = 127.0178924

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        gymSearchService.searchGymDtoList() >> gymList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetGymName == "에이블짐 교대역점"
        results.get(1).targetGymName == "에이블짐 신논현역"
    }
}
