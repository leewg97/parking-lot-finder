package com.parking.project.parkinglot.service


import com.parking.project.parkinglot.entity.ParkingLot
import com.parking.project.parkinglot.repository.ParkingLotRepository
import org.springframework.beans.factory.annotation.Autowired

class ParkingLotRepositoryServiceTest extends com.parking.project.AbstractIntegrationContainerBaseTest {

    @Autowired
    private ParkingLotRepositoryService parkingLotRepositoryService

    @Autowired
    private ParkingLotRepository parkingLotRepository

    def setup() {
        parkingLotRepository.deleteAll()
    }

    def "GymRepository update - dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 서초구 논현동"
        String modifiedAddress = "서울 특별시 송파구 송파동"
        String name = "방이고분"

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(inputAddress)
                .parkingLotName(name)
                .build()

        when:
        def entity = parkingLotRepository.save(parkingLot)
        parkingLotRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = parkingLotRepository.findAll()

        then:
        result.get(0).getParkingLotAddress() == modifiedAddress
    }

    def "GymRepository update - dirty checking fail"() {
        given:
        String inputAddress = "서울 특별시 서초구 논현동"
        String modifiedAddress = "서울 특별시 송파구 송파동"
        String name = "방이고분"

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(inputAddress)
                .parkingLotName(name)
                .build()

        when:
        def entity = parkingLotRepository.save(parkingLot)
        parkingLotRepositoryService.updateAddressWithoutTransaction(entity.getId(), modifiedAddress)

        def result = parkingLotRepository.findAll()

        then:
        result.get(0).getParkingLotAddress() == inputAddress
    }

    def "self invocation"() {

        given:
        String address = "서울특별시 서초구 논현로5길29"
        String name = "구름공원구립주차정"
        double latitude = 37.47
        double longitude = 127.04

        def parkingLot = ParkingLot.builder()
                .parkingLotAddress(address)
                .parkingLotName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        parkingLotRepositoryService.bar(Arrays.asList(parkingLot))

        then:
        def e = thrown(RuntimeException.class)
        def result = parkingLotRepositoryService.findAll()
        result.size() == 1 // 트랜잭션이 적용되지 않는다(롤백 적용 X)
    }

    def "transactional readOnly test - 읽기 전용일 경우 dirty checking 반영 되지 않는다. "() {

        given:
        String inputAddress = "서울 특별시 서초구"
        String modifiedAddress = "서울 특별시 송파구"
        String name = "방이고분"
        double latitude = 37.49
        double longitude = 127.10

        def input = ParkingLot.builder()
                .parkingLotAddress(inputAddress)
                .parkingLotName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def parkingLot = parkingLotRepository.save(input)
        parkingLotRepositoryService.startReadOnlyMethod(parkingLot.id)

        then:
        def result = parkingLotRepositoryService.findAll()
        result.get(0).getParkingLotAddress() == inputAddress
    }

}
