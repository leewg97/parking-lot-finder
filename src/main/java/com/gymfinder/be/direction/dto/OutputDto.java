package com.gymfinder.be.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String gymName;
    private String gymAddress;
    private String directionUrl;    // 길안내 url
    private String roadViewUrl;     // 로드뷰 url
    private String distance;        // 고객 주소와 시설 주소의 거리

}
