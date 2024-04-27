package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class GptAPI {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/startTest")
	public String processRequest(@RequestBody StartTestRequest startTestRequest) {
        	
	    String tutorType = startTestRequest.getTutorType();
        String content = startTestRequest.getContent();

        // TODO: Implement the logic to process the request
        
        
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
