import React, { useState } from "react";
import { Form, Button, ListGroup, Spinner } from "react-bootstrap";
import axios from "axios";

const ChatInterface = ({ onLogout, onViewHistory }) => {
  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [loading, setLoading] = useState(false); // New state for loading

  const handleSendMessage = async () => {
    if (message.trim() === "") return;

    const newMessage = { text: message, type: "user" };
    setChatHistory((prevHistory) => [...prevHistory, newMessage]);

    setLoading(true);

    try {
      const response = await axios.post("/api/images/suggest-recipes", null, {
        params: { ingredients: message },
      });

      const formattedReply = formatMessage(response.data);
      const reply = { text: formattedReply, type: "bot" };
      setChatHistory((prevHistory) => [...prevHistory, reply]);
    } catch (error) {
      console.error("Error sending message:", error);
      const errorReply = {
        text: "Failed to get recipe suggestions. Please try again.",
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
    const formData = new FormData();
    formData.append("file", file);

    setLoading(true);

    axios
      .post("/api/images/upload", formData)
      .then((response) => {
        const formattedReply = formatMessage(response.data);
        const reply = { text: formattedReply, type: "bot" };
        setChatHistory((prevHistory) => [...prevHistory, reply]);
      })
      .catch((error) => {
        console.error("Error uploading file:", error);
        const errorReply = {
          text: "Failed to upload image. Please try again.",
          type: "bot",
        };
        setChatHistory((prevHistory) => [...prevHistory, errorReply]);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const formatMessage = (message) => {
    return message
      .replace(/### (.*?)\n/g, "<h3>$1</h3>") // Formats headers
      .replace(/#### (.*?)\n/g, "<h4>$1</h4>") // Formats sub-headers
      .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>") // Formats bold text
      .replace(/- (.*?)\n/g, "<li>$1</li>") // Formats list items
      .replace(/1\.\s/g, "<ol><li>") // Starts an ordered list
      .replace(/\d\.\s/g, "</li><li>") // Continues the ordered list
      .replace(/<\/li>$/g, "</li></ol>") // Closes the ordered list
      .replace(/\n/g, "<br/>"); // Adds line breaks
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
            <ListGroup.Item
              key={index}
              className={msg.type === "bot" ? "response-message" : ""}
              dangerouslySetInnerHTML={{ __html: msg.text }}
            />
          ))}
        </ListGroup>
        {loading && <Spinner animation="border" style={{ margin: "0.5rem" }} />}
      </div>
      <div className="chat-footer">
        <Form.Control
          type="text"
          placeholder="Type your ingredients..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <Button
          variant="primary"
          onClick={handleSendMessage}
          style={{ margin: "0.5rem" }}
        >
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
