package com.gymfinder.be.direction.service;

import com.gymfinder.be.api.dto.DocumentDto;
import com.gymfinder.be.api.service.KakaoCategorySearchService;
import com.gymfinder.be.direction.entity.Direction;
import com.gymfinder.be.direction.repository.DirectionRepository;
import com.gymfinder.be.gym.service.GymSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 5; // 최대 겁색 갯수
    private static final double RADIUS_KM = 10.0; // 반경 10km

    private final GymSearchService gymSearchService;
    private final DirectionRepository directionRepository;
    private final KakaoCategorySearchService kakaoCategorySearchService;

    public List<Direction> buildDirectionList(DocumentDto dto) {
        if (Objects.isNull(dto)) return Collections.emptyList();

        return gymSearchService.searchGymDtoList()
                .stream()
                .map(gymDto ->
                        Direction.builder()
                                .inputAddress(dto.getAddressName())
                                .inputLatitude(dto.getLatitude())
                                .inputLongitude(dto.getLongitude())
                                .targetGymName(gymDto.getGymName())
                                .targetAddress(gymDto.getGymAddress())
                                .targetLatitude(gymDto.getLatitude())
                                .targetLongitude(gymDto.getLongitude())
                                .distance(
                                        calculateDistance(
                                                dto.getLatitude(), dto.getLongitude(),
                                                gymDto.getLatitude(), gymDto.getLongitude()
                                        )
                                )
                                .build()
                )
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // facility search by category kakao api
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
                                .targetGymName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
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
