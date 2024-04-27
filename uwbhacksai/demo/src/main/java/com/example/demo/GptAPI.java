package com.example.demo;

import com.example.demo.tutor.SpeechToTextService;
import com.example.demo.tutor.TextToSpeechService;
import com.example.demo.tutor.TutorLLMService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class GptAPI {

    private final TutorLLMService tutorLLMService;
    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;

    public GptAPI(TutorLLMService tutorLLMService,
                  SpeechToTextService speechToTextService,
                  TextToSpeechService textToSpeechService) {
        this.tutorLLMService = tutorLLMService;
        this.speechToTextService = speechToTextService;
        this.textToSpeechService = textToSpeechService;
    }

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@PostMapping("/startTest")
	public String processRequest(@RequestBody StartTestRequest startTestRequest) {
        	
	    String tutorType = startTestRequest.getTutorType();
        String content = startTestRequest.getContent();

        // Step 1: Get text response from LLM
        String tutorResponseText = tutorLLMService.startConversation(tutorType, content);

        // Step 2: Convert text response to speech
        try {
            textToSpeechService.tts(tutorResponseText);
        } catch (Exception e) {
            return "Error converting text to speech";
        }

        // TODO: Send audio file as response
        
       	return tutorResponseText;
    }

    @PostMapping(value = "/sendStudentResponse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> processStudentResponse(@RequestParam("file") MultipartFile file) {
        try {
            // Step 1: Convert student's audio response to text
            byte[] audioData = file.getBytes();
            String transcribedStudentResponse = speechToTextService.getTranscription(
                    audioData, file.getOriginalFilename()
            );

            // Step 2: Process the student's response and generate a tutor response
            String tutorResponse = tutorLLMService.processResponse(transcribedStudentResponse);

            // TODO: Implement text-to-speech conversion and return the audio file as a response
            return null; // TODO: REMOVE THIS WHEN IMPLEMENTATION IS DONE  <--------------------------------------------
//            // Step 3: Convert the tutor response to speech and get the audio data
//            byte[] tutorResponseAudioData = textToSpeechService.tts(tutorResponse);
//
//            // Step 4: Create a resource from the tutor response audio data
//            Resource resource = new ByteArrayResource(tutorResponseAudioData);
//
//            return ResponseEntity.ok()
//                    .contentLength(tutorResponseAudioData.length)
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tutor_response.mp3\"")
//                    .body(resource);

        } catch (IOException e) {
            // Handle the case where the file couldn't be read
            Logger.getAnonymousLogger().severe("Error reading the audio file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error processing the audio file".getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            // Handle other exceptions, such as transcription errors or text-to-speech errors
            Logger.getAnonymousLogger().severe("Error processing the audio file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource("Error during processing".getBytes(StandardCharsets.UTF_8)));
        }
    }

}
