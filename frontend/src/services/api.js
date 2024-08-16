import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export const setAuthToken = (userToken) => {
  localStorage.setItem("token", userToken); // Store token in localStorage
  axios.defaults.headers.common["Authorization"] = `Bearer ${userToken}`;
};

export const loginUser = async (username, password) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/users/login`,
      { username, password },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const registerUser = async (username, password) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/users/register`,
      { username, password }, // Send payload as JSON
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const logoutUser = () => {
  localStorage.removeItem("token"); // Remove token from localStorage
  delete axios.defaults.headers.common["Authorization"]; // Remove token from axios default headers
};

export const getCurrentUser = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users/current`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getUserImageHistory = async () => {
  try {
    // Ensure the token is set in axios defaults
    const token = localStorage.getItem("token");
    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
    const response = await axios.get(`${BASE_URL}/users/history`);
    return response.data;
  } catch (error) {
    throw error;
  }
};
