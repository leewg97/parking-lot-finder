package com.gymfinder.be.gym.service;

import com.gymfinder.be.api.dto.DocumentDto;
import com.gymfinder.be.api.dto.KakaoApiResponseDto;
import com.gymfinder.be.api.service.KakaoAddressSearchService;
import com.gymfinder.be.direction.dto.OutputDto;
import com.gymfinder.be.direction.entity.Direction;
import com.gymfinder.be.direction.service.Base62Service;
import com.gymfinder.be.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymFindingService {

    private final Base62Service base62Service;
    private final DirectionService directionService;
    private final KakaoAddressSearchService kakaoAddressSearchService;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";

    @Value("${gym.finder.base.url}")
    private String baseUrl;

    public List<OutputDto> searchGymList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[GymSearchService searchGymList fail] Input address : {}", address);
            return Collections.emptyList();
        }

        DocumentDto dto = kakaoApiResponseDto.getDocumentList().get(0);
        // kakao 카테고리를 이용한 장소 겸색 api 이용
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(dto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction) {
        return OutputDto.builder()
                .gymName(direction.getTargetGymName())
                .gymAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId()))
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetGymName() + "," + direction.getTargetLatitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }

}
