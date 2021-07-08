package com.example.demo.dto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TargetResponse {
    private List<String> data;

    public TargetResponse(OriginalResponse original) {
        this.data = original.getData().stream()
                .map(o -> String.join("|", o.getId(), o.getLastName(), o.getEmail()))
                .collect(toList());
    }

    public List<String> getData() {
        return data;
    }
}
