package com.example.recipes_suggester.controller;

import com.example.recipes_suggester.model.User;
import com.example.recipes_suggester.model.User.ImageHistory;
import com.example.recipes_suggester.service.S3Service;
import com.example.recipes_suggester.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            String imageUrl = s3Service.uploadFile(file, username);
            User user = userService.findByUsername(username);
            if (user != null) {
                ImageHistory imageHistory = new ImageHistory(imageUrl, LocalDateTime.now().toString());
                user.getImageHistory().add(imageHistory);
                userService.saveUser(user);
            }
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
