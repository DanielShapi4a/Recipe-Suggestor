import React, { useState, useEffect } from "react";
import { ListGroup, Button } from "react-bootstrap";
import { getUserImageHistory } from "../../services/api";

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

const HistoryView = ({ onBack }) => {
  const [imageHistory, setImageHistory] = useState([]);

  useEffect(() => {
    const fetchImageHistory = async () => {
      try {
        const history = await getUserImageHistory();
        setImageHistory(history);
      } catch (error) {
        console.error("Failed to fetch image history:", error);
      }
    };

    fetchImageHistory();
  }, []);

  return (
    <div>
      <Button variant="secondary" onClick={onBack} style={{ margin: "0.5rem" }}>
        Back to Chat
      </Button>
      <ListGroup>
        {imageHistory.length > 0 ? (
          imageHistory.map((item, index) => (
            <ListGroup.Item key={index}>
              {/* Display image if available */}
              {item.imageUrl && (
                <div style={{ marginBottom: "10px" }}>
                  <img
                    src={item.imageUrl}
                    alt={`Uploaded at ${item.uploadTimestamp}`}
                    style={{
                      maxWidth: "100%",
                      maxHeight: "200px",
                      borderRadius: "8px",
                    }}
                  />
                </div>
              )}
              <p>
                Uploaded on: {new Date(item.uploadTimestamp).toLocaleString()}
              </p>

              {/* Display conversation */}
              <div>
                {item.conversation.map((message, msgIndex) => {
                  // Skip bot messages if they match the formatted recipe
                  const isRecipeMessage = item.recipes.some(
                    (recipe) =>
                      formatMessage(recipe) === formatMessage(message.message)
                  );

                  if (message.type === "user" || !isRecipeMessage) {
                    return (
                      <div
                        key={msgIndex}
                        style={{
                          backgroundColor:
                            message.type === "user" ? "#e0f7fa" : "#f1f8e9",
                          padding: "10px",
                          borderRadius: "5px",
                          marginBottom: "10px",
                          textAlign: message.type === "user" ? "left" : "right",
                        }}
                      >
                        {message.message}
                      </div>
                    );
                  }
                  return null;
                })}
              </div>

              {/* Display recipe */}
              <div>
                {item.recipes.map((recipe, recipeIndex) => (
                  <div
                    key={recipeIndex}
                    dangerouslySetInnerHTML={{ __html: formatMessage(recipe) }}
                    style={{
                      backgroundColor: "#fff3e0",
                      padding: "10px",
                      borderRadius: "5px",
                      whiteSpace: "pre-wrap",
                      marginBottom: "10px",
                    }}
                  />
                ))}
              </div>
            </ListGroup.Item>
          ))
        ) : (
          <div>No history yet</div>
        )}
      </ListGroup>
      <Button variant="secondary" onClick={onBack} style={{ margin: "0.5rem" }}>
        Back to Chat
      </Button>
    </div>
  );
};

export default HistoryView;
