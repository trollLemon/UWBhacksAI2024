package com.example.demo.voiceAnalysis;

import com.example.demo.ExternalApiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class VoiceAnalysisService {

    @Value("${behavioral.clientId}")
    public String behavioralClientId;

    private OkHttpClient okHttpClient;

    private final List<VoiceAnalysisReport> reports = new ArrayList<>();

    public VoiceAnalysisService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void resetReports() {
        reports.clear();
    }

    public List<VoiceAnalysisReport> getReports() {
        return reports;
    }

    /**
     * Queue the analysis of a voice file.
     *
     * @param file the voice file to be analyzed
     */
    @Async
    public CompletableFuture<Void> asyncQueueAnalysis(MultipartFile file) throws IOException {
        return asyncQueueAnalysis(file.getBytes(), file.getOriginalFilename(), file.getContentType());
    }

    /**
     * Queue the analysis of a voice file.
     *
     * @param audioData   the voice file data
     * @param filename    the voice file name
     * @param contentType the voice file content type
     */
    @Async
    public CompletableFuture<Void> asyncQueueAnalysis(byte[] audioData, String filename, String contentType) {
        return CompletableFuture.runAsync(() -> analyzeVoice(audioData, filename, contentType));
    }

    private void analyzeVoice(byte[] audioData, String filename, String contentType) {
        // Prepare the request body from the file
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", filename,
                        RequestBody.create(audioData, MediaType.parse(Objects.requireNonNull(contentType))))
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url("https://api.behavioralsignals.com/client/" + behavioralClientId + "/process/audio?channels=1&calldirection=1&predictionmode=full")
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "multipart/form-data")
                .build();

        // Execute the call synchronously
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                // Handle the error response
                throw new IOException("Unexpected code " + response);
            }

            // Parse the response to create a VoiceAnalysisReport object
            String responseBody = response.body().string();
            VoiceAnalysisReport report = parseResponseToReport(responseBody);

            // Append the result in the reports list, this operation must be thread-safe
            synchronized (reports) {
                reports.add(report);
            }
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe("Failed to analyze voice file: " + e.getMessage());
        }
    }

    private VoiceAnalysisReport parseResponseToReport(String responseBody) throws IOException {
        // Initialize the Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        // Parse the JSON response to a JsonNode object
        JsonNode rootNode = mapper.readTree(responseBody);

        // Extract the relevant pieces of information from the JSON
        JsonNode coreNode = rootNode.path("core");
        double strength = 0.0;
        double hesitation = 0.0;
        double positivity = 0.0;
        double engagement = 0.0;

        // We assume the values for agent and customer are directly under the "core" node.
        if (coreNode.has("strength")) {
            JsonNode strengthNode = coreNode.path("strength");
            strength = (strengthNode.path("customer").asDouble());
        }
        if (coreNode.has("hesitation")) {
            JsonNode hesitationNode = coreNode.path("hesitation");
            hesitation = (hesitationNode.path("customer").asDouble());
        }
        if (coreNode.has("positivity")) {
            JsonNode positivityNode = coreNode.path("positivity");
            positivity = (positivityNode.path("customer").asDouble());
        }
        if (coreNode.has("engagement")) {
            JsonNode engagementNode = coreNode.path("engagement");
            engagement = (engagementNode.path("customer").asDouble());
        }

        // Create and return the VoiceAnalysisReport object
        return new VoiceAnalysisReport(strength, hesitation, positivity, engagement);
    }

}