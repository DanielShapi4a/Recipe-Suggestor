package com.example.recipes_suggester.model;

public class ImageUploadResponse {
    private String imageUrl;
    private String analysisResult;

    public ImageUploadResponse(String imageUrl, String analysisResult) {
        this.imageUrl = imageUrl;
        this.analysisResult = analysisResult;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    @Override
    public String toString() {
        return String.format("Image URL: %s\nAnalysis Result: %s", imageUrl, analysisResult);
    }
}
