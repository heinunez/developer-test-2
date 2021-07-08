package com.example.demo.service;

import com.example.demo.dto.OriginalResponse;
import com.example.demo.dto.TargetResponse;
import com.example.demo.exception.CannotFetchOriginalRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ResponseTransformServiceImpl implements ResponseTransformService {
    private final String sourceUrl;
    private final RestTemplate restTemplate;
    private final HttpEntity<String> userAgentEntity;

    public ResponseTransformServiceImpl(@Value("${source.request.url}") String sourceUrl, RestTemplate restTemplate, HttpEntity<String> userAgentEntity) {
        this.sourceUrl = sourceUrl;
        this.restTemplate = restTemplate;
        this.userAgentEntity = userAgentEntity;
    }

    @Override
    public TargetResponse transform() throws CannotFetchOriginalRequestException {
        OriginalResponse original = fetchOriginal();
        return new TargetResponse(original);
    }

    private OriginalResponse fetchOriginal() throws CannotFetchOriginalRequestException {
        try {
            return restTemplate.exchange(
                    sourceUrl,
                    HttpMethod.GET,
                    userAgentEntity,
                    OriginalResponse.class
            )
                    .getBody();
        } catch (Exception e) {
            throw new CannotFetchOriginalRequestException(e);
        }
    }
}
