import React, { useState } from "react";
import { Container, Navbar, Nav } from "react-bootstrap";
import LoginModal from "./components/LoginModal";
import ChatInterface from "./components/chatinterface/ChatInterface";
import HistoryView from "./components/historyview/HistoryView";
import "./theme/theme.css";

const App = () => {
  const [showModal, setShowModal] = useState(true);
  const [user, setUser] = useState(null);
  const [showChat, setShowChat] = useState(false);
  const [showHistory, setShowHistory] = useState(false);

  const handleLogin = (username) => {
    setUser(username);
    setShowModal(false);
    setShowChat(true);
  };

  const handleLogout = () => {
    setUser(null);
    setShowModal(true);
    setShowChat(false);
    setShowHistory(false);
  };

  const handleViewHistory = () => {
    setShowChat(false);
    setShowHistory(true);
  };

  const handleBackToChat = () => {
    setShowHistory(false);
    setShowChat(true);
  };

  return (
    <div className="App">
      <Navbar bg="light" expand="lg" className="navbar">
        <Container>
          <Navbar.Brand href="#home">Recipe Suggester👨‍🍳</Navbar.Brand>
          <Nav className="ml-auto">
            {user && (
              <>
                <Navbar.Text style={{ fontWeight: "500", color: "black" }}>
                  Signed in as: <a href="#login">{user} </a>
                </Navbar.Text>
              </>
            )}
          </Nav>
        </Container>
      </Navbar>

      <Container className="mt-3">
        {showModal && !showChat && !showHistory && (
          <LoginModal
            show={showModal}
            handleClose={() => setShowModal(false)}
            handleLogin={handleLogin}
            backdrop="static"
            keyboard={false}
          />
        )}

        {showChat && !showHistory && (
          <ChatInterface
            onLogout={handleLogout}
            onViewHistory={handleViewHistory}
          />
        )}

        {showHistory && <HistoryView onBack={handleBackToChat} />}
      </Container>
    </div>
  );
};

export default App;
