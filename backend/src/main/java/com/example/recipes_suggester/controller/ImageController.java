package com.example.recipes_suggester.controller;

import com.example.recipes_suggester.model.User;
import com.example.recipes_suggester.model.User.ImageHistory;
import com.example.recipes_suggester.service.S3Service;
import com.example.recipes_suggester.service.UserService;
import com.example.recipes_suggester.service.ChatGPTHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description) {

        // Get the authentication object to retrieve the username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Log the authenticated user's name for debugging
        if (authentication != null) {
            String username = authentication.getName();
            System.out.println("Authenticated user: " + username);

            try {
                // Upload the image to S3
                String imageUrl = s3Service.uploadFile(file, username);

                // Analyze the image and get recipe suggestions from ChatGPT
                String recipeMessage;
                if (description == null || description.trim().isEmpty()) {
                    // Use base logic if no description is provided
                    recipeMessage = chatGPTHttpService.analyzeImageAndSuggestRecipes(imageUrl);
                } else {
                    // Include the description in the prompt
                    recipeMessage = chatGPTHttpService.analyzeImageWithDescriptionAndSuggestRecipes(imageUrl, description);
                }

                // Save image details and recipe message to user history
                User user = userService.findByUsername(username);
                if (user != null) {
                    ImageHistory imageHistory = new ImageHistory();
                    imageHistory.setImageUrl(imageUrl);
                    imageHistory.setUploadTimestamp(LocalDateTime.now().toString());
                    imageHistory.setRecipes(Arrays.asList(recipeMessage));

                    // Add conversation messages
                    List<User.ImageHistory.ConversationMessage> conversation = new ArrayList<>();
                    conversation.add(new User.ImageHistory.ConversationMessage("user", description != null ? description : "No description provided"));
                    conversation.add(new User.ImageHistory.ConversationMessage("bot", recipeMessage));
                    imageHistory.setConversation(conversation);

                    user.getImageHistory().add(imageHistory);
                    userService.saveUser(user);
                }

                return ResponseEntity.ok(recipeMessage);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload file. Please try again.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An unexpected error occurred. Please try again.");
            }
        } else {
            System.out.println("No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }


    @PostMapping("/suggest-recipes")
    public ResponseEntity<String> suggestRecipes(@RequestParam("ingredients") String ingredients) {
        // Get the authentication object to retrieve the username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Log the authenticated user's name for debugging
        if (authentication != null) {
            String username = authentication.getName();
            System.out.println("Authenticated user: " + username);

            try {
                // Delegate recipe suggestion to ChatGPTHttpService
                String recipeSuggestions = chatGPTHttpService.suggestRecipes(ingredients);

                // Save conversation history
                User user = userService.findByUsername(username);
                if (user != null) {
                    ImageHistory imageHistory = new ImageHistory();
                    imageHistory.setUploadTimestamp(LocalDateTime.now().toString());
                    imageHistory.setRecipes(Arrays.asList(recipeSuggestions));

                    // Add conversation messages
                    List<ImageHistory.ConversationMessage> conversation = new ArrayList<>();
                    conversation.add(new User.ImageHistory.ConversationMessage("user", ingredients));
                    conversation.add(new User.ImageHistory.ConversationMessage("bot", recipeSuggestions));
                    imageHistory.setConversation(conversation);

                    user.getImageHistory().add(imageHistory);
                    userService.saveUser(user);
                }

                return ResponseEntity.ok(recipeSuggestions);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to get recipe suggestions. Please try again.");
            }
        } else {
            System.out.println("No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

}
