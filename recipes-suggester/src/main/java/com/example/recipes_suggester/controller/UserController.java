package com.example.recipes_suggester.controller;

import com.example.recipes_suggester.model.User;
import com.example.recipes_suggester.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestParam String username, @RequestParam String password) {
        return userService.registerUser(username, password);
    }

    @PostMapping("/login")
    public User loginUser(@RequestParam String username, @RequestParam String password) {
        return userService.authenticateUser(username, password);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test endpoint is working!";
    }

    @GetMapping("/current")
    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return "User is not logged in";
            }

            String currentUserName = authentication.getName();
            return "Logged in as: " + currentUserName;
        } catch (Exception e) {
            return "An error occurred while retrieving user information, try to log-in again";
        }
    }


    @GetMapping("/history")
    public List<User.ImageHistory> getUserImageHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByUsername(currentUserName);
        if (user != null){
            return user.getImageHistory();
        } else {
            return new ArrayList<>();
        }
    }
}
