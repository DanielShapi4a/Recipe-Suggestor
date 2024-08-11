import React, { useState } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import { loginUser, registerUser } from "../services/api";

const LoginModal = ({ show, handleClose, handleLogin, backdrop, keyboard }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isRegister, setIsRegister] = useState(false);
  const [error, setError] = useState("");

  const handleRegistration = async () => {
    try {
      const registerResponse = await registerUser(username, password);
      if (registerResponse && registerResponse.id) {
        const loginResponse = await loginUser(username, password);
        if (loginResponse && loginResponse.status === "success") {
          handleLogin(username);
          handleClose();
        } else {
          setError(
            "Registration successful, but login failed. Please try logging in manually."
          );
        }
      } else {
        setError("Registration failed. Please try again.");
      }
    } catch (error) {
      setError(error.response ? error.response.data.message : error.message);
    }
  };

  const handleLoginUser = async () => {
    try {
      const response = await loginUser(username, password);
      if (response && response.status === "success") {
        handleLogin(username);
        handleClose();
      } else {
        setError("Invalid username or password.");
      }
    } catch (error) {
      setError(error.response ? error.response.data.message : error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (isRegister) {
      await handleRegistration();
    } else {
      await handleLoginUser();
    }
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      backdrop={backdrop}
      keyboard={keyboard}
    >
      <Modal.Header closeButton>
        <Modal.Title>{isRegister ? "Register" : "Login"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="formUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </Form.Group>
          <Form.Group controlId="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
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
