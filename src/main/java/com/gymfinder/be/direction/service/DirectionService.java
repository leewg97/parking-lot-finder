package com.gymfinder.be.direction.service;

import com.gymfinder.be.api.dto.DocumentDto;
import com.gymfinder.be.direction.entity.Direction;
import com.gymfinder.be.gym.service.GymSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
