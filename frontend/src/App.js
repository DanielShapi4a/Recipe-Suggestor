import React, { useState } from "react";
import { Container, Navbar, Nav } from "react-bootstrap";
import LoginModal from "./components/LoginModal";
import "./theme/theme.css";

const App = () => {
  const [showModal, setShowModal] = useState(true);
  const [user, setUser] = useState(null);

  const handleLogin = (username, password, isRegister) => {
    // Implement login or registration logic here
    // For now, we'll just set the user
    setUser(username);
    setShowModal(false);
  };

  const handleLogout = () => {
    setUser(null);
    setShowModal(true);
  };

  return (
    <div className="App">
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">Recipe Suggester</Navbar.Brand>
          <Nav className="ml-auto">
            {user ? (
              <>
                <Nav.Link href="#" onClick={handleLogout}>
                  Logout
                </Nav.Link>
                <Navbar.Text className="ml-2">
                  Signed in as: <a href="#login">{user}</a>
                </Navbar.Text>
              </>
            ) : (
              <Nav.Link href="#" onClick={() => setShowModal(true)}>
                Login
              </Nav.Link>
            )}
          </Nav>
        </Container>
      </Navbar>

      <Container className="mt-3">
        <h1>Welcome to Recipe Suggester</h1>
        {/* Add more content and components here */}
      </Container>

      <LoginModal
        show={showModal}
        handleClose={() => setShowModal(false)}
        handleLogin={handleLogin}
      />
    </div>
  );
};

export default App;
