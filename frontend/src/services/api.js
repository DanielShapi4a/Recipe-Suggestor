import axios from "axios";

const BASE_URL = "http://localhost:8080/api";

export const loginUser = async (username, password) => {
  try {
    const response = await axios.post(`${BASE_URL}/users/login`, null, {
      params: { username, password },
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const registerUser = async (username, password) => {
  try {
    const response = await axios.post(`${BASE_URL}/users/register`, {
      username,
      password,
    },{
    withCredentials: true,
    });
    return response.data;
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
    throw error;
  }
};

export const getCurrentUser = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users/current`, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};

export const getUserImageHistory = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/users/history`, {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    throw error.response ? error.response.data : error.message;
  }
};
