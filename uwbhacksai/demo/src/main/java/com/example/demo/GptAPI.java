package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

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

class Audio{}

@RestController
@RequestMapping("/api")
public class GptAPI {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/startTest")
	public String processRequest(@RequestBody Request request) {
        	
	    String tutorType = request.getTutorType();
        String content = request.getContent();
        
        
       	return "Request processed successfully";
    }

	@PostMapping(value = "/sendStudentResponse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile file) {
//	try {
//            byte[] data = audioService.processAudio(file);
//            Resource resource = new ByteArrayResource(data);
//
//            return ResponseEntity.ok()
//                    .contentLength(data.length)
//                    .contentType(MediaType.parseMediaType("audio/mpeg"))
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }

	    return ResponseEntity.internalServerError().build();
	}

}
