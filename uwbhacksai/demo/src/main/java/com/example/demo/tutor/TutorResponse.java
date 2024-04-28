package com.example.demo.tutor;

public class TutorResponse {
    private String textResponse;
    private boolean isDone;

    public TutorResponse(String textResponse, boolean isDone) {
        this.textResponse = textResponse;
        this.isDone = isDone;
    }

    public String getTextResponse() {
        return textResponse;
    }

    public boolean isDone() {
        return isDone;
    }
}
