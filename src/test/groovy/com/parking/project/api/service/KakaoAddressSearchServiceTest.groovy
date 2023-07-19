package com.parking.project.api.service

import com.parking.project.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService

    def "주소 파라미터 값이 null 이면, requestAddressSearch 메소드는 null 을 리턴한다."() {
        given:
        String address = null

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 유효하다면, requestAddressSearch 메소드는 정상적으로 document 를 반환한다."() {
        given:
        def address = "서울 서초구 사평대로 371"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList.get(0).addressName != null
    }

    def "정상적인 주소를 입력했을 경우, 정상적인 위도 경도로 반환된다."() {
        given:
        boolean actualResult = false

        when:
        def searchResult = kakaoAddressSearchService.requestAddressSearch(inputAddress)

        then:
        if (searchResult == null) actualResult = false
        else actualResult = searchResult.getDocumentList().size() > 0

        where:
        inputAddress         | expectedResult
        "서울 서초구 사평대로"        | true
        "서울 강남구 논현동 91"      | true
        "서울 대학로"             | true
        "서울 강남구 잘못된 주소"      | false
        "송파구 송파동 243"        | true
        "송파구 잠실동 243-243333" | false
        ""                   | false
    }

}