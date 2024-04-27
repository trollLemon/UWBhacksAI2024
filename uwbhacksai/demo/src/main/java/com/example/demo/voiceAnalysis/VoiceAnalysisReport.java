package com.example.demo.voiceAnalysis;

public class VoiceAnalysisReport {
    private double strength;
    private double hesitation;
    private double positivity;
    private double engagement;

    public VoiceAnalysisReport(double strength, double hesitation, double positivity, double engagement) {
        this.strength = strength;
        this.hesitation = hesitation;
        this.positivity = positivity;
        this.engagement = engagement;
    }

    public double getStrength() {
        return strength;
    }

    public double getHesitation() {
        return hesitation;
    }

    public double getPositivity() {
        return positivity;
    }

    public double getEngagement() {
        return engagement;
    }
}
