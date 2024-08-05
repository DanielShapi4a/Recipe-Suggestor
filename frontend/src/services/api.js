import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export const loginUser = async (username, password) => {
  try {
    const response = await axios.post("http://localhost:8080/api/users/login", {
      username,
      password,
    });
    return response.data; // Ensure this matches the response structure
  } catch (error) {
    throw error;
  }
};

export const registerUser = async (username, password) => {
  try {
    const response = await axios.post(
      "http://localhost:8080/api/users/register",
      {
        username,
        password,
      }
    );
    return response.data; // Ensure this matches the response structure
  } catch (error) {
    throw error;
  }
};

export const logoutUser = async () => {
  try {
    const response = await axios.post(
      `${BASE_URL}/users/logout`,
      {},
      { withCredentials: true }
    );
    return response.data;
  } catch (error) {
    console.error("Error logging out:", error);
    throw error;
  }
};

export const getCurrentUser = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users/current`);
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};

export const getUserImageHistory = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users/history`);
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};

export const uploadImage = async (file) => {
  try {
    const formData = new FormData();
    formData.append("file", file);

    const response = await axios.post(`${BASE_URL}/images/upload`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};

export const suggestRecipes = async (ingredients) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/images/suggest-recipes`,
      null,
      {
        params: { ingredients },
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      }
    );
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};
