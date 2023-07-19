package com.parking.project.direction.service;

import com.parking.project.api.dto.DocumentDto;
import com.parking.project.api.service.KakaoCategorySearchService;
import com.parking.project.direction.entity.Direction;
import com.parking.project.direction.repository.DirectionRepository;
import com.parking.project.parkinglot.service.ParkingLotSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 10; // 최대 겁색 갯수
    private static final double RADIUS_KM = 20.0; // 반경 20km
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    private final Base62Service base62Service;
    private final ParkingLotSearchService parkingLotSearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;

    public String findDirectionUrlById(String encodedId) {
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        assert direction != null;
        String params = String.join(",", direction.getTargetParkingLotName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        // 시설 이름이 한글이기 때문에 인코딩
        return UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();
    }


    public List<Direction> buildDirectionList(DocumentDto dto) {
        if (Objects.isNull(dto)) return Collections.emptyList();

        return parkingLotSearchService.searchParkingLotDtoList()
                .stream()
                .map(parkingLotDto ->
                        Direction.builder()
                                .inputAddress(dto.getAddressName())
                                .inputLatitude(dto.getLatitude())
                                .inputLongitude(dto.getLongitude())
                                .targetParkingLotName(parkingLotDto.getParkingLotName())
                                .targetAddress(parkingLotDto.getParkingLotAddress())
                                .targetLatitude(parkingLotDto.getLatitude())
                                .targetLongitude(parkingLotDto.getLongitude())
                                .distance(
                                        calculateDistance(
                                                dto.getLatitude(), dto.getLongitude(),
                                                parkingLotDto.getLatitude(), parkingLotDto.getLongitude()
                                        )
                                )
                                .build()
                )
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // parking lot search by category kakao api
    public List<Direction> buildDirectionListByCategoryApi(DocumentDto inputDocumentDto) {
        if (Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return kakaoCategorySearchService
                .requestFacilityCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentList()
                .stream()
                .map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetParkingLotName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001)
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if (CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);
    }

    // HAVERSINE FORMULA
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }

}
