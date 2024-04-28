package com.example.demo;

import com.example.demo.tutor.SpeechToTextService;
import com.example.demo.tutor.TextToSpeechService;
import com.example.demo.tutor.TutorLLMService;
import com.example.demo.tutor.TutorResponse;
import com.example.demo.voiceAnalysis.VoiceAnalysisService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class GptAPI {
    private Boolean isGradingTime = false;

    private String lastTutorTextBuff;
    private String lastStudentTextBuff;

    private final TutorLLMService tutorLLMService;
    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;
    private final VoiceAnalysisService voiceAnalysisService;

    public GptAPI(TutorLLMService tutorLLMService,
                  SpeechToTextService speechToTextService,
                  TextToSpeechService textToSpeechService,
                  VoiceAnalysisService voiceAnalysisService) {
        this.tutorLLMService = tutorLLMService;
        this.speechToTextService = speechToTextService;
        this.textToSpeechService = textToSpeechService;
        this.voiceAnalysisService = voiceAnalysisService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }


    private ResponseEntity<Resource> _sendAudio(byte[] tutorResponseAudioData) throws Exception {
        Resource resource = new ByteArrayResource(tutorResponseAudioData);

        return ResponseEntity.ok()
                .contentLength(tutorResponseAudioData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tutor_response.wav\"")
                .body(resource);
    }


    @GetMapping("/tutorTranscript")
    public ResponseEntity<String> returnLastTranscript() {
        return ResponseEntity.ok().contentLength(lastTutorTextBuff.length())
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(lastTutorTextBuff);
    }

    @GetMapping("/studentTranscript")
    public ResponseEntity<String> returnLastStudentTranscript() {
        return ResponseEntity.ok().contentLength(lastStudentTextBuff.length())
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(lastStudentTextBuff);
    }


    @GetMapping("/isGradingTime")
    public ResponseEntity<Boolean> shouldGrade() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(isGradingTime);
    }

    @PostMapping("/startTest")
    public ResponseEntity<Resource> processRequest(@RequestBody StartTestRequest startTestRequest) {

        String tutorType = startTestRequest.getTutorType();
        String content = startTestRequest.getContent();

        try {
            // Step 1: Get text response from LLM
            String tutorResponseText = tutorLLMService.startConversation(content, tutorType);

            // Step 2: Convert text response to speech
            lastTutorTextBuff = tutorResponseText;
            byte[] tutorResponseAudioData = textToSpeechService.tts(tutorResponseText);

            // Step 4: Create a resource from the tutor response audio data
            return _sendAudio(tutorResponseAudioData);

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

    @PostMapping(value = "/sendStudentResponse")
    public ResponseEntity<Resource> processStudentResponse(MultipartFile file) {
        try {
            // Add logging to inspect received file
            System.out.println("Received file: " + file.getOriginalFilename());

            // Step 1: Queue up behavioral analysis and convert student's audio response to text
            voiceAnalysisService.asyncQueueAnalysis(file);

            // Get the transcription of the student's response
            String transcribedStudentResponse = speechToTextService.getTranscription(
                    file.getBytes(), file.getOriginalFilename()
            );
            lastStudentTextBuff = transcribedStudentResponse;

            // Step 2: Process the student's response and generate a tutor response
            TutorResponse tutorResponse = tutorLLMService.processResponse(transcribedStudentResponse);
            String tutorTextResponse = tutorLLMService.processResponse(transcribedStudentResponse).getTextResponse();
            lastTutorTextBuff = tutorTextResponse;

            // Check if the tutor is done grading
            isGradingTime = tutorResponse.isDone();

            // Step 3: Convert the tutor response to speech and get the audio data
            byte[] tutorResponseAudioData = textToSpeechService.tts(tutorTextResponse);

            // Step 4: Create a resource from the tutor response audio data
            return _sendAudio(tutorResponseAudioData);
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

    @GetMapping(value = "/gradingReport")
    public String gradingReport() {
        return tutorLLMService.getGradingResults();
    }

}
