package com.example.recipes_suggester.controller;

import com.example.recipes_suggester.model.User;
import com.example.recipes_suggester.security.JwtTokenProvider;
import com.example.recipes_suggester.security.LoginRequest;
import com.example.recipes_suggester.security.LoginResponse;
import com.example.recipes_suggester.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtTokenProvider.generateToken(user.getUsername());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<String> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
            }

            String currentUserName = authentication.getName();
            return ResponseEntity.ok("Logged in as: " + currentUserName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving user information");
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserImageHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();
            User user = userService.findByUsername(currentUserName);

            if (user != null) {
                List<Map<String, Object>> historyList = new ArrayList<>();

                for (User.ImageHistory history : user.getImageHistory()) {
                    Map<String, Object> historyMap = new HashMap<>();
                    historyMap.put("imageUrl", history.getImageUrl());
                    historyMap.put("uploadTimestamp", history.getUploadTimestamp());
                    historyMap.put("recipes", history.getRecipes());
                    historyMap.put("conversation", history.getConversation());
                    historyList.add(historyMap);
                }

                return ResponseEntity.ok(historyList);
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving image history");
        }
    }
}