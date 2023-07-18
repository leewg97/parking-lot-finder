package com.gymfinder.be.gym.repository


import com.gymfinder.be.gym.entity.Gym
import com.gymfinder.be.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

class GymRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private GymRepository gymRepository

    def setup() {
        gymRepository.deleteAll()
    }

    def "GymRepository save"() {
        given:
        String address = "서울 서초구 반포동"
        String name = "에이블짐 신논현역점"
        double latitude = 37.50
        double longitude = 127.01

        def gym = Gym.builder()
                .gymAddress(address)
                .gymName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = gymRepository.save(gym)

        then:
        result.getGymAddress() == address
        result.getGymName() == name
        result.getLatitude() == latitude
        result.getLongitude() == longitude
    }

    def "GymRepository saveAll"() {
        given:
        String address = "서울 서초구 반포동"
        String name = "에이블짐 신논현역점"
        double latitude = 37.50
        double longitude = 127.01

        def gym = Gym.builder()
                .gymAddress(address)
                .gymName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        gymRepository.saveAll(Arrays.asList(gym))
        def result = gymRepository.findAll()

        then:
        result.size() == 1
    }

    def "BaseTimeEntity 등록"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "서울 서초구 반포동"
        String name = "에이블짐 신논현역점"

        def gym = Gym.builder()
                .gymAddress(address)
                .gymName(name)
                .build()

        when:
        gymRepository.save(gym)
        def result = gymRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }

}
