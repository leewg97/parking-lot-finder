package com.gymfinder.be.gym.service;

import com.gymfinder.be.api.dto.DocumentDto;
import com.gymfinder.be.api.dto.KakaoApiResponseDto;
import com.gymfinder.be.api.service.KakaoAddressSearchService;
import com.gymfinder.be.direction.entity.Direction;
import com.gymfinder.be.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymFindingService {

    private final DirectionService directionService;
    private final KakaoAddressSearchService kakaoAddressSearchService;

    public void searchGymList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[GymSearchService searchGymList fail] Input address : {}", address);
            return;
        }

        DocumentDto dto = kakaoApiResponseDto.getDocumentList().get(0);
//        List<Direction> directionList = directionService.buildDirectionList(dto);
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(dto);
        directionService.saveAll(directionList);
    }
}
