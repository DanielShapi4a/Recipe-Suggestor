package com.example.recipes_suggester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class RecipesSuggesterApplication {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));

        SpringApplication.run(RecipesSuggesterApplication.class, args);
    }
}
