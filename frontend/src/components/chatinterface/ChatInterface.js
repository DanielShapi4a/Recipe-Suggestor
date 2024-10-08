import React, { useState } from "react";
import { Form, Button, ListGroup, Spinner } from "react-bootstrap";
import axios from "axios";
import "./ChatInterface.css";

const ChatInterface = ({ onLogout, onViewHistory }) => {
  const API_URI = "http://localhost:8080/api";
  const token = localStorage.getItem("token"); // Retrieve token from localStorage

  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [image, setImage] = useState(null);

  const handleSendMessage = async () => {
    if (message.trim() === "" && !image) return;

    setLoading(true);

    try {
      let response;

      if (image) {
        const formData = new FormData();
        formData.append("file", image);
        formData.append("description", message.trim() || "");

        response = await axios.post(`${API_URI}/images/upload`, formData, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setImage(null);
      } else {
        response = await axios.post(`${API_URI}/images/suggest-recipes`, null, {
          params: { ingredients: message },
          headers: { Authorization: `Bearer ${token}` },
        });
      }

      const formattedReply = formatMessage(response.data);
      const reply = { text: formattedReply, type: "bot" };
      setChatHistory((prevHistory) => [...prevHistory, reply]);
    } catch (error) {
      console.error("Error:", error);
      const errorReply = {
        text: "Failed to process request. Please try again.",
        type: "bot",
      };
      setChatHistory((prevHistory) => [...prevHistory, errorReply]);
    } finally {
      setLoading(false);
    }

    setMessage("");
  };

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImage(file);
    }
  };

  const formatMessage = (message) => {
    return message
      .replace(/### (.*?)\n/g, "<h3>$1</h3>")
      .replace(/#### (.*?)\n/g, "<h4>$1</h4>")
      .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
      .replace(/- (.*?)\n/g, "<li>$1</li>")
      .replace(/1\.\s/g, "<ol><li>")
      .replace(/\d\.\s/g, "</li><li>")
      .replace(/<\/li>$/g, "</li></ol>")
      .replace(/\n/g, "<br/>");
  };

  return (
    <div className="chat-interface">
      <div className="chat-header">
        <Button className="loggin-button" onClick={onLogout}>
          Logout
        </Button>
        <Button className="view-history-button" onClick={onViewHistory}>
          View History
        </Button>
      </div>
      <div className="chat-body">
        <ListGroup>
          {chatHistory.map((msg, index) => (
            <ListGroup.Item
              key={index}
              className={msg.type === "bot" ? "response-message" : ""}
              dangerouslySetInnerHTML={{ __html: msg.text }}
            />
          ))}
          {image && (
            <ListGroup.Item className="image-preview">
              <img
                src={URL.createObjectURL(image)}
                alt="Uploaded preview"
                style={{ maxWidth: "100%", maxHeight: "200px" }}
              />
              <p>{message}</p>
            </ListGroup.Item>
          )}
        </ListGroup>
        {loading && <Spinner animation="border" style={{ margin: "0.5rem" }} />}
      </div>
      <div className="chat-footer">
        <Form.Control
          type="text"
          placeholder="Type your description or instructions..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <Button variant="primary" onClick={handleSendMessage}>
          Send
        </Button>
        <input
          type="file"
          accept="image/*"
          onChange={handleFileUpload}
          style={{ margin: "0.5rem" }}
        />
      </div>
    </div>
  );
};

export default ChatInterface;
