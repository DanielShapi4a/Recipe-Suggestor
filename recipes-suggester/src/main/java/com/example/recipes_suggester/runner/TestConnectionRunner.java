package com.example.recipes_suggester.runner;

import com.example.recipes_suggester.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestConnectionRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Testing MongoDB connection...");
        long count = userRepository.count();
        System.out.println("Number of users in database: " + count);
    }
}
