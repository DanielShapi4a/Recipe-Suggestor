package com.example.recipes_suggester.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Users")
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private List<ImageHistory> imageHistory = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ImageHistory> getImageHistory() {
        return imageHistory;
    }

    public void setImageHistory(List<ImageHistory> imageHistory) {
        this.imageHistory = imageHistory;
    }

    public static class ImageHistory {
        private String imageUrl;
        private String uploadTimestamp;
        private List<String> recipes;
        private List<ConversationMessage> conversation;

        public ImageHistory() {
        }

        public ImageHistory(String imageUrl, String uploadTimestamp) {
            this.imageUrl = imageUrl;
            this.uploadTimestamp = uploadTimestamp;
            this.recipes = new ArrayList<>();
            this.conversation = new ArrayList<>();
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getUploadTimestamp() {
            return uploadTimestamp;
        }

        public void setUploadTimestamp(String uploadTimestamp) {
            this.uploadTimestamp = uploadTimestamp;
        }

        public List<String> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<String> recipes) {
            this.recipes = recipes;
        }

        public List<ConversationMessage> getConversation() {
            return conversation;
        }

        public void setConversation(List<ConversationMessage> conversation) {
            this.conversation = conversation;
        }

        public static class ConversationMessage {
            private String type; // 'user' or 'bot'
            private String message;

            public ConversationMessage() {
            }

            public ConversationMessage(String type, String message) {
                this.type = type;
                this.message = message;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
