package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the parameters used to start a conversational test session.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartTestRequest {
    @JsonProperty("tutorType")
    private String tutorType;

    @JsonProperty("content")
    private String content;

    // Default constructor
    public StartTestRequest() {
    }

    // Getters and setters
    public String getTutorType() {
        return tutorType;
    }

    public void setTutorType(String tutorType) {
        this.tutorType = tutorType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
