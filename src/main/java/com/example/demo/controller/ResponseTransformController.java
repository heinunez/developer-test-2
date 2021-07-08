package com.example.demo.controller;

import com.example.demo.dto.TargetResponse;
import com.example.demo.exception.CannotFetchOriginalRequestException;
import com.example.demo.service.ResponseTransformService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transform")
public class ResponseTransformController {
    private final ResponseTransformService service;

    public ResponseTransformController(ResponseTransformService service) {
        this.service = service;
    }

    @PostMapping
    public TargetResponse transform() throws CannotFetchOriginalRequestException {
        return service.transform();
    }
}
