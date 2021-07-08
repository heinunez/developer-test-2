package com.example.demo.service;

import com.example.demo.dto.TargetResponse;
import com.example.demo.exception.CannotFetchOriginalRequestException;

public interface ResponseTransformService {
    TargetResponse transform() throws CannotFetchOriginalRequestException;
}
