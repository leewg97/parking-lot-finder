package com.gymfinder.be.gym.service;

import com.gymfinder.be.gym.dto.GymDto;
import com.gymfinder.be.gym.entity.Gym;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GymSearchService {

    private final GymRepositoryService gymRepositoryService;

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
