package com.example.demo.voiceAnalysis;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VoiceAnalysisService {

    private OkHttpClient okHttpClient;

    public VoiceAnalysisService(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * Queue the analysis of a voice file.
     * @param file
     */
    public void queueAnalysis(MultipartFile file) {

    }

    public void analyzeVoice() {

    }

}
