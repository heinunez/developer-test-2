package com.example.demo.service;

import com.example.demo.dto.OriginalData;
import com.example.demo.dto.OriginalResponse;
import com.example.demo.dto.TargetResponse;
import com.example.demo.exception.CannotFetchOriginalRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseTransformServiceTest {
    private ResponseTransformService service;
    private RestTemplate template;
    private OriginalData expectedData;

    @BeforeEach
    void beforeEach() {
        template = mock(RestTemplate.class);
        service = new ResponseTransformServiceImpl("", template, null);

        OriginalResponse originalResponse = new OriginalResponse();
        expectedData = new OriginalData("1", "mail@mail", "nunez");
        originalResponse.setData(singletonList(expectedData));

        mockExchangeMethod()
                .thenReturn(new ResponseEntity(originalResponse, HttpStatus.OK));
    }

    @Test
    void givenSuccessfulOriginalRequestWhenCallsToTransformThenResponseIsCorrect() throws CannotFetchOriginalRequestException {
        TargetResponse response = service.transform();
        assertThat(response.getData(), is(notNullValue()));
        assertFalse(response.getData().isEmpty());
        assertThat(response.getData().get(0), is(String.join("|", expectedData.getId(), expectedData.getLastName(), expectedData.getEmail())));
    }

    @Test
    void givenErrorOnOriginalRequestWhenCallsToTransformThenThrowsAnException() throws CannotFetchOriginalRequestException {
        mockExchangeMethod().thenThrow(new RuntimeException());
        try {
            service.transform();
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(CannotFetchOriginalRequestException.class));
        }
    }


    private OngoingStubbing<ResponseEntity<OriginalResponse>> mockExchangeMethod() {
        return when(template.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(OriginalResponse.class),
                Matchers.<String>anyVararg()
                )
        );
    }
}