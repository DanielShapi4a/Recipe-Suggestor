import React, { useState } from "react";
import { Form, Button, ListGroup } from "react-bootstrap";
import axios from "axios";

const ChatInterface = ({ onLogout, onViewHistory }) => {
  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);

  const handleSendMessage = async () => {
    if (message.trim() === "") return;

    const newMessage = { text: message, type: "user" };
    setChatHistory((prevHistory) => [...prevHistory, newMessage]);

    try {
      const response = await axios.post("/api/images/suggest-recipes", null, {
        params: { ingredients: message },
      });
      const reply = { text: response.data, type: "bot" };
      setChatHistory((prevHistory) => [...prevHistory, reply]);
    } catch (error) {
      console.error("Error sending message:", error);
      const errorReply = {
        text: "Failed to get recipe suggestions. Please try again.",
        type: "bot",
      };
      setChatHistory((prevHistory) => [...prevHistory, errorReply]);
    }

    setMessage("");
  };

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    const formData = new FormData();
    formData.append("file", file);

    axios
      .post("/api/images/upload", formData)
      .then((response) => {
        const reply = { text: response.data, type: "bot" };
        setChatHistory((prevHistory) => [...prevHistory, reply]);
      })
      .catch((error) => {
        console.error("Error uploading file:", error);
        const errorReply = {
          text: "Failed to upload image. Please try again.",
          type: "bot",
        };
        setChatHistory((prevHistory) => [...prevHistory, errorReply]);
      });
  };

  return (
    <div className="chat-interface">
      <div className="chat-header">
        <Button variant="secondary" onClick={onLogout}>
          Logout
        </Button>
        <Button variant="secondary" onClick={onViewHistory}>
          View History
        </Button>
      </div>
      <div className="chat-body">
        <ListGroup>
          {chatHistory.map((msg, index) => (
            <ListGroup.Item key={index} className={msg.type}>
              {msg.text}
            </ListGroup.Item>
          ))}
        </ListGroup>
      </div>
      <div className="chat-footer">
        <Form.Control
          type="text"
          placeholder="Type your ingredients..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <Button variant="primary" onClick={handleSendMessage}>
          Send
        </Button>
        <input type="file" accept="image/*" onChange={handleFileUpload} />
      </div>
    </div>
  );
};

export default ChatInterface;