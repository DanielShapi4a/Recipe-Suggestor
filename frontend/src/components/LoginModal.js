import React, { useState } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import { loginUser, registerUser } from "../services/api";

const LoginModal = ({ show, handleClose, handleLogin }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isRegister, setIsRegister] = useState(false);
  const [error, setError] = useState(""); // State to manage error messages

  // Handle user registration
  const handleRegistration = async () => {
    try {
      const response = await registerUser(username, password);
      console.log("Response from registration is:", response);

      if (response && response.id) {
        // Registration successful, now log in
        handleLogin(username);
        handleClose();
        setIsRegister(false); // Switch to login mode
      } else {
        setError("Registration failed. Please try again.");
      }
    } catch (error) {
      console.error("Registration failed:", error);
      setError(error.response ? error.response.data.message : error.message);
    }
  };

  // Handle user login
  const handleLoginUser = async () => {
    try {
      const response = await loginUser(username, password);
      console.log("Response from login is:", response);

      if (response && response.status === "success") {
        handleLogin(username);
        handleClose();
      } else {
        setError("Invalid username or password.");
      }
    } catch (error) {
      console.error("Login failed:", error);
      setError(error.response ? error.response.data.message : error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); // Reset error message on each submit

    if (isRegister) {
      // Handle registration
      await handleRegistration();
    } else {
      // Handle login
      await handleLoginUser();
    }
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>{isRegister ? "Register" : "Login"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}{" "}
        {/* Display error message */}
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="formUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </Form.Group>

          <Form.Group controlId="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </Form.Group>

          <Button variant="primary" type="submit" style={{ marginTop: "10px" }}>
            {isRegister ? "Register" : "Login"}
          </Button>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={() => setIsRegister(!isRegister)}>
          {isRegister
            ? "Already have an account? Login"
            : "Don't have an account? Register"}
        </Button>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default LoginModal;
