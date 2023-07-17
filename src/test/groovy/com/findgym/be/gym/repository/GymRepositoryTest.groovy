package com.findgym.be.gym.repository

import com.findgym.be.gym.entity.Gym
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class GymRepositoryTest extends Specification {

    @Autowired
    private GymRepository gymRepository

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

}
