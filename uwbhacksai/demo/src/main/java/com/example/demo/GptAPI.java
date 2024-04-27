package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.fasterxml.jackson.annotation.JsonProperty;

 class Request {
    @JsonProperty("tutorType")
    private String tutorType;

    @JsonProperty("content")
    private String content;

    // Default constructor
    public Request() {
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

@RestController
public class GptAPI {


	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/api/startTest")
	public String processRequest(@RequestBody Request request) {
        	
	String tutorType = request.getTutorType();
        String content = request.getContent();
        
        
       	return "Request processed successfully";
    }

}
