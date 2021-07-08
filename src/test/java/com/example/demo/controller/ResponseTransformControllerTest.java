package com.example.demo.controller;

import com.example.demo.dto.OriginalData;
import com.example.demo.dto.OriginalResponse;
import com.example.demo.dto.TargetResponse;
import com.example.demo.exception.CannotFetchOriginalRequestException;
import com.example.demo.service.ResponseTransformService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResponseTransformControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ResponseTransformService service;

    @Test
    void givenSuccessfulOriginalRequestWhenCallsToTransformThenResponseIsCorrect() throws Exception {
        OriginalResponse original = new OriginalResponse();
        OriginalData expectedData = new OriginalData("1", "mail@mail", "nunez");
        original.setData(singletonList(expectedData));
        doReturn(new TargetResponse(original))
                .when(service).transform();

        mockMvc.perform(
                post("/transform")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", equalTo(String.join("|", expectedData.getId(), expectedData.getLastName(), expectedData.getEmail()))));
    }

    @Test
    void givenErrorOnOriginalRequestWhenCallsToTransformThenResponseIsCorrect() throws Exception {
        doThrow(new CannotFetchOriginalRequestException(new RuntimeException()))
                .when(service).transform();

        mockMvc.perform(
                post("/transform")
        )
                .andExpect(status().isInternalServerError());
    }
}