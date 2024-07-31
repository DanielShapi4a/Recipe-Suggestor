package com.example.recipes_suggester.repository;

import com.example.recipes_suggester.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
