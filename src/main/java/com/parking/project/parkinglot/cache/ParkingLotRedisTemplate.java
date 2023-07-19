package com.parking.project.parkinglot.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.project.parkinglot.dto.ParkingLotDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingLotRedisTemplate {

    private static final String CACHE_KEY = "PARKING";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(ParkingLotDto parkingLotDto) {
        if(Objects.isNull(parkingLotDto) || Objects.isNull(parkingLotDto.getId())) {
            log.error("Required Values must not be null");
            return;
        }

        try {
            hashOperations.put(CACHE_KEY,
                    parkingLotDto.getId().toString(),
                    serializeParkingLotDto(parkingLotDto));
            log.info("[ParkingLotRedisTemplateService save success] id: {}", parkingLotDto.getId());
        } catch (Exception e) {
            log.error("[ParkingLotRedisTemplateService save error] {}", e.getMessage());
        }
    }

    public List<ParkingLotDto> findAll() {

        try {
            List<ParkingLotDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                ParkingLotDto parkingLotDto = deserializeParkingLotDto(value);
                list.add(parkingLotDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[ParkingLotRedisTemplateService findAll error]: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[ParkingLotRedisTemplateService delete]: {} ", id);
    }

    private String serializeParkingLotDto(ParkingLotDto parkingLotDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(parkingLotDto);
    }

    private ParkingLotDto deserializeParkingLotDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, ParkingLotDto.class);
    }
}
