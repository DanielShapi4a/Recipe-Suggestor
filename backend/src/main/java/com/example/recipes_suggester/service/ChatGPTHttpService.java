package com.example.recipes_suggester.service;

import com.amazonaws.services.rekognition.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatGPTHttpService {

    @Value("${OPENAI_KEY}")
    private String apiKey;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String awsRegion;

    private static final Logger logger = LoggerFactory.getLogger(ChatGPTHttpService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String analyzeImageAndSuggestRecipes(String s3ObjectUrl) {
        logger.info("Bucket Name: {}", bucketName);
        logger.info("S3 Object Key: {}", s3ObjectUrl);
        logger.info("AWS Region: {}", awsRegion);
        // Analyze the image using AWS Rekognition
        List<String> ingredients = analyzeImageWithRekognition(s3ObjectUrl);

        // If no ingredients found, return an appropriate message
        if (ingredients.isEmpty()) {
            return "No ingredients detected in the image.";
        }

        // Format the ingredients into a prompt for ChatGPT
        String ingredientsList = String.join(", ", ingredients);
        return suggestRecipes(ingredientsList);
    }

    public String analyzeImageWithDescriptionAndSuggestRecipes(String s3ObjectUrl, String description) throws Exception {
        logger.info("Bucket Name: {}", bucketName);
        logger.info("S3 Object Key: {}", s3ObjectUrl);
        logger.info("AWS Region: {}", awsRegion);

        // Analyze the image using AWS Rekognition
        List<String> ingredients = analyzeImageWithRekognition(s3ObjectUrl);

        // If no ingredients found, return an appropriate message
        if (ingredients.isEmpty()) {
            return "No ingredients detected in the image.";
        }

        // Format the ingredients into a prompt for ChatGPT
        String ingredientsList = String.join(", ", ingredients);
        if (description == null || description.trim().isEmpty()) {
            // Use the suggestRecipes method to get a suggestion based on the ingredients list
            return suggestRecipes(ingredientsList);
        } else {
            // Create a prompt that includes the description and ingredients list
            String prompt = String.format(
                    "Based on the user's input: '%s' and the ingredients: %s, suggest a recipe or provide help with cooking steps. If the user's request is unrelated to recipes or cooking, respond with a polite message stating that you only assist with recipe suggestions and cooking-related queries. Do not mention the ingredients in your response unless it's directly related to the request.",
                    description,
                    ingredientsList
            );

            // Use the suggestRecipes method with the updated prompt
            return suggestRecipes(prompt);
        }
    }

    public String suggestRecipes(String ingredients) {
        try {
            // Format the ingredients into a prompt for ChatGPT
            String prompt = String.format("Based on the ingredients in this text suggest recipe to use them in without mentioning the text itself: %s. ", ingredients);

            // Create the request body
            String body = String.format("""
            {
                "model": "gpt-4o",
                "messages": [
                    {
                        "role": "user",
                        "content": "%s"
                    }
                ]
            }""", prompt);

            // Create the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            // Send the request and get the response
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Extract the recipe message from the response
            return extractRecipeMessage(response.body());

        } catch (Exception e) {
            throw new RuntimeException("Failed to get recipe suggestions", e);
        }
    }

    private String extractRecipeMessage(String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode messageContent = root.path("choices").get(0).path("message").path("content");
        return messageContent.asText();
    }

    private List<String> analyzeImageWithRekognition(String s3ObjectUrl) {
        String s3ObjectKey = extractKeyFromUrl(s3ObjectUrl);
        logger.info("Extracted S3 Object Key: {}", s3ObjectKey);

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(awsRegion)
                .build();

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withBucket(bucketName)
                                .withName(s3ObjectKey)))
                .withMaxLabels(10)
                .withMinConfidence(75F);

        try {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List<Label> labels = result.getLabels();

            return labels.stream()
                    .map(Label::getName)
                    .collect(Collectors.toList());

        } catch (AmazonRekognitionException e) {
            logger.error("Failed to analyze image with Rekognition", e);
            throw new RuntimeException("Failed to analyze image with Rekognition", e);
        }
    }

    private String extractKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath().substring(1); // Remove leading '/'
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract S3 key from URL", e);
        }
    }
}
