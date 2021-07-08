package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OriginalData {
    private String id;
    private String email;
    @JsonProperty("last_name")
    private String lastName;

    public OriginalData(String id, String email, String lastName) {
        this.id = id;
        this.email = email;
        this.lastName = lastName;
    }

    public OriginalData() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }


}
