package com.findgym.be.gym.service

import com.findgym.be.AbstractIntegrationContainerBaseTest
import com.findgym.be.gym.repository.GymRepository
import org.springframework.beans.factory.annotation.Autowired

class GymRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private GymRepositoryService gymRepositoryService;;

    @Autowired
    private GymRepository gymRepository;

    def setup() {
        gymRepository.deleteAll()
    }

    def "GymRepository update - dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 서초구 논현동"
        String modifiedAddress = "서울 특별시 송파구 송파동"
        String name = "에이블짐"

        def gym = Gym.builder()
                .gymAddress(inputAddress)
                .gymName(name)
                .build()

        when:
        def entity = gymRepository.save(gym)
        gymRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = gymRepository.findAll()

        then:
        result.get(0).getGymAddress() == modifiedAddress
    }

    def "GymRepository update - dirty checking fail"() {
        given:
        String inputAddress = "서울 특별시 서초구 논현동"
        String modifiedAddress = "서울 특별시 송파구 송파동"
        String name = "에이블짐"

        def gym = Gym.builder()
                .gymAddress(inputAddress)
                .gymName(name)
                .build()

        when:
        def entity = gymRepository.save(gym)
        gymRepositoryService.updateAddressWithoutTransactional(entity.getId(), modifiedAddress)

        def result = gymRepository.findAll()

        then:
        result.get(0).getGymAddress() == inputAddress
    }

    def "self invocation"() {

        given:
        String address = "서울 특별시 서초구 논현동"
        String name = "에이블짐"
        double latitude = 37.50
        double longitude = 127.01

        def gym = Gym.builder()
                .gymAddress(address)
                .gymName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        gymRepositoryService.bar(Arrays.asList(gym))

        then:
        def e = thrown(RuntimeException.class)
        def result = gymRepositoryService.findAll()
        result.size() == 1 // 트랜잭션이 적용되지 않는다(롤백 적용 X)
    }

    def "transactional readOnly test - 읽기 전용일 경우 dirty checking 반영 되지 않는다. "() {

        given:
        String inputAddress = "서울 특별시 서초구"
        String modifiedAddress = "서울 특별시 송파구"
        String name = "에이블짐"
        double latitude = 37.50
        double longitude = 127.01

        def input = Gym.builder()
                .gymAddress(inputAddress)
                .gymName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def gym = gymRepository.save(input)
        gymRepositoryService.startReadOnlyMethod(gym.id)

        then:
        def result = gymRepositoryService.findAll()
        result.get(0).getGymAddress() == inputAddress
    }

}
