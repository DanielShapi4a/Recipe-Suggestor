package com.example.recipes_suggester.controller;

import com.example.recipes_suggester.model.User;
import com.example.recipes_suggester.model.User.ImageHistory;
import com.example.recipes_suggester.service.S3Service;
import com.example.recipes_suggester.service.UserService;
import com.example.recipes_suggester.service.ChatGPTHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatGPTHttpService chatGPTHttpService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            // Upload the image to S3
            String imageUrl = s3Service.uploadFile(file, username);

            // Analyze the image and get recipe suggestions from ChatGPT
            String recipeMessage = chatGPTHttpService.analyzeImageAndSuggestRecipes(imageUrl);

            // Save image details and recipe message to user history
            User user = userService.findByUsername(username);
            if (user != null) {
                ImageHistory imageHistory = new ImageHistory();
                imageHistory.setImageUrl(imageUrl);
                imageHistory.setUploadTimestamp(LocalDateTime.now().toString());
                imageHistory.setRecipes(Arrays.asList(recipeMessage));

                user.getImageHistory().add(imageHistory);
                userService.saveUser(user);
            }

            return recipeMessage;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file. Please try again.";
        } catch (Exception e) {
            e.printStackTrace();
            return "An unexpected error occurred. Please try again.";
        }
    }

    @PostMapping("/suggest-recipes")
    public String suggestRecipes(@RequestParam("ingredients") String ingredients) {
        try {
            // Delegate recipe suggestion to ChatGPTHttpService
            return chatGPTHttpService.suggestRecipes(ingredients);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to get recipe suggestions. Please try again.";
        }
    }
}
