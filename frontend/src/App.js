import React, { useState } from "react";
import { Container, Navbar, Nav } from "react-bootstrap";
import LoginModal from "./components/LoginModal";
import ChatInterface from "./components/ChatInterface";
import "./theme/theme.css";

const App = () => {
  const [showModal, setShowModal] = useState(true);
  const [user, setUser] = useState(null);
  const [showChat, setShowChat] = useState(false);

  const handleLogin = (username) => {
    setUser(username);
    setShowModal(false);
    setShowChat(true);
  };

  const handleLogout = () => {
    setUser(null);
    setShowModal(true);
    setShowChat(false);
  };

  const handleViewHistory = () => {
    // Implement history viewing logic here
  };

  return (
    <div className="App">
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">Recipe Suggester</Navbar.Brand>
          <Nav className="ml-auto">
            {user && (
              <>
                <Nav.Link href="#" onClick={handleLogout}>
                  Logout
                </Nav.Link>
                <Navbar.Text className="ml-2">
                  Signed in as: <a href="#login">{user}</a>
                </Navbar.Text>
              </>
            )}
          </Nav>
        </Container>
      </Navbar>

      <Container className="mt-3">
        {showModal && !showChat && (
          <LoginModal
            show={showModal}
            handleClose={() => setShowModal(false)}
            handleLogin={handleLogin}
          />
        )}

        {showChat && (
          <ChatInterface
            onLogout={handleLogout}
            onViewHistory={handleViewHistory}
          />
        )}
      </Container>
    </div>
  );
};

export default App;
