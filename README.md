<h1 align="center">🍳 Recipe Suggester Application</h1>
<h3 align="center">Welcome to the Recipe Suggester Application👨‍🍳🥙🍜 This project combines the power of AWS Rekognition and OpenAI's ChatGPT to help you discover delicious recipes from the ingredients in your kitchen. Whether you upload images of food or directly provide a list of ingredients, this app will suggest recipes tailored to your needs.</h3>

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Setup and Installation](#setup-and-installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- 🔒 **User Registration and Authentication**: Secure user registration and login functionality.
- 📸 **Image Upload and Analysis**: Upload food images to AWS S3, analyze them with AWS Rekognition, and get recipe suggestions from ChatGPT.
- 💬 **Direct Recipe Chat**: Ask for recipe suggestions based on a list of ingredients without uploading an image.
- 📜 **User History**: Track uploaded images and associated recipes for each user.
- 🧹 **Clean JSON Handling**: Save only the relevant parts of the ChatGPT response in the user history.

## 🛠️ Technology Stack

- **Backend**: Java, Spring Boot, Spring Security
- **Frontend**: React 
- **Database**: MongoDB For users, AWS S3 for images
- **Cloud Services**: AWS S3, AWS Rekognition
- **AI Services**: OpenAI's ChatGPT

## 🚀 Setup and Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/recipe-suggester.git
    cd recipe-suggester
    ```

2. **Configure Environment Variables**:
    Create a `.env` file in the root directory and add your AWS and OpenAI credentials:
    ```env
    AWS_ACCESS_KEY=your_aws_access_key_id
    AWS_SECRET_KEY=your_aws_secret_access_key
    OPENAI_KEY=your_openai_api_key
    ```

3. **Build and Run the Application**:
    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the Application**:
    Open your browser and go to `http://localhost:8080`.

## 📝 Usage

### Register a New User

Send a POST request to `/register` with the following JSON body:
```json
{
  "username": "your_username",
  "password": "your_password"
}
```
### Log in
Send a POST request to /login with the following JSON body:
```json
{
  "username": "your_username",
  "password": "your_password"
}
```
### Upload an Image
Send a POST request to /upload with the image file as form data.

Get Recipe Suggestions Based on Ingredients
Send a POST request to /chat with the following JSON body:
```json
{
  "ingredients": "list of ingredients"
}
```

## 📂 Endpoints
- POST /register: Register a new user.
- POST /login: Log in with existing user credentials.
- POST /upload: Upload an image for analysis.
- POST /recipe-suggestion: Get recipe suggestions based on ingredients.

## 📚 Examples
Example User Data in MongoDB
```json
{
  "_id": "66ab88319a4b29483e0c8a86",
  "username": "dan",
  "password": "$2a$10$zakIrD9q9RlkxdKq02v67O/Ys2b7Tbvf.xqFNLlSvGybAgQreWGNu",
  "imageHistory": [
    {
      "imageUrl": "https://imagerecipeanalysis.s3.eu-central-1.amazonaws.com/uploads/dan/images.png",
      "uploadTimestamp": "2024-08-03T14:00:58.682071500",
      "recipes": [
        "1. Birthday Cake French Toast: Ingredients: Bread, Birthday Cake chunks, Eggs, Milk, Sugar, Vanilla Extract, Whipped Cream. Decorate with colored sugar or sprinkles for a fun festive breakfast or brunch.",
        "2. Pancake Cake: Instead of making regular pancakes, layer them with a cream or custard between each one. Ensure the pancakes are the same size, and once stacked with cream in between each one, it should resemble a cake."
      ]
    }
  ]
}
```
Example Chat Response
```json
{
  "ingredients": "eggs, flour, sugar",
  "recipes": [
    "1. Classic Pancakes: Ingredients: Eggs, Flour, Sugar, Milk, Baking Powder. Mix all ingredients and cook on a griddle until golden brown.",
    "2. Simple Cake: Ingredients: Eggs, Flour, Sugar, Butter, Baking Powder. Mix all ingredients, pour into a pan, and bake at 350°F for 30 minutes."
  ]
}
```
## 🔧 Application Properties Configuration
The application.properties file should be created in the backend/src/main/resources directory with the following format:
```propeeties
{
# Server configuration
server.port=8080

# MongoDB configuration
spring.data.mongodb.uri=your_mongodb_connection_string

# AWS S3 configuration
aws.s3.bucketName=your_s3_bucket_name
aws.s3.region=your_s3_region
aws.access.key.id=your_aws_access_key_id
aws.secret.access.key=your_aws_secret_access_key

# OpenAI API key
openai.api.key=your_openai_api_key

# JSON output formatting
spring.jackson.serialization.indent-output=true

# Frontend URI (if applicable)
frontend.uri=http://localhost:3000
}
```
Replace your_mongodb_connection_string, your_s3_bucket_name, your_s3_region, your_aws_access_key_id, your_aws_secret_access_key, and your_openai_api_key with your actual credentials.

## 🤝 Contributing
We welcome contributions to improve this project! Here are some ways you can help:
- 🐞 Report Bugs: If you find any bugs, please open an issue.
- 🌟 Request Features: If you have ideas for new features, please open an issue.
- 👩‍💻 Submit Pull Requests: If you can code, feel free to fork the repository and submit a pull request.

