package com.parking.project.direction.controller

import com.parking.project.direction.dto.OutputDto
import com.parking.project.parkinglot.service.ParkingLotFindingService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private ParkingLotFindingService gymFindingService = Mock()
    private List<OutputDto> outputDtoList

    def setup() {
        // FormController MockMvc 객체로 만든다.
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(gymFindingService))
                .build()

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .parkingLotName("parking lot 1")
                        .build(),
                OutputDto.builder()
                        .parkingLotName("parking lot 2")
                        .build()
        )
    }

    def "GET /"() {
        expect:
        // FormController 의 "/" URI 를 get 방식으로 호출
        mockMvc.perform(get("/"))
                .andExpect(handler().handlerType(FormController.class))
                .andExpect(handler().methodName("main"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andDo(log())
    }

    def "POST /search"() {
        given:
        String inputAddress = "서울 강남구 논현동"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))

        then:
        1 * gymFindingService.searchParkingLotList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("output"))
                .andExpect(model().attributeExists("outputFormList"))           // model에 outputFormList라는 key가 존재하는지 확인. 즉, name에 해당하는 데이터가 model에 있는지 검증
                .andExpect(model().attribute("outputFormList", outputDtoList))  // name에 해당하는 데이터가 value 객체인지 검증
                .andDo(print())
    }
}
