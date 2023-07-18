package com.gymfinder.be.gym.service;

import com.gymfinder.be.api.dto.DocumentDto;
import com.gymfinder.be.api.dto.KakaoApiResponseDto;
import com.gymfinder.be.api.service.KakaoAddressSearchService;
import com.gymfinder.be.direction.entity.Direction;
import com.gymfinder.be.direction.service.DirectionService;
import com.gymfinder.be.gym.dto.GymDto;
import com.gymfinder.be.gym.entity.Gym;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymSearchService {

    private final DirectionService directionService;
    private final GymRepositoryService gymRepositoryService;
    private final KakaoAddressSearchService kakaoAddressSearchService;

    public void searchGymList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[GymSearchService searchGymList fail] Input address : {}", address);
            return;
        }

        DocumentDto dto = kakaoApiResponseDto.getDocumentList().get(0);
        List<Direction> directionList = directionService.buildDirectionList(dto);
        directionService.saveAll(directionList);
    }

    public List<GymDto> searchGymDtoList() {
        // TODO: redis

        // db
        return gymRepositoryService.findAll()
                .stream()
                .map(this::convertToGymDto)
                .collect(Collectors.toList());
    }

    private GymDto convertToGymDto(Gym gym) {
        return GymDto.builder()
                .id(gym.getId())
                .gymAddress(gym.getGymAddress())
                .gymName(gym.getGymName())
                .latitude(gym.getLatitude())
                .longitude(gym.getLongitude())
                .build();
    }

}
