import axios from 'axios';

const config = {
  baseUrl: 'http://localhost:8080/api',
};

const fetchLoginStatus = async () => {
  try {
    return await axios.get(config.baseUrl, { withCredentials: true });
  } catch (error) {
    console.log("api/index.js", error.response);
    throw Error("You are not logged in");
  }
};

const fetchLogout = async () => {
  try {
    return await axios.get(`${config.baseUrl}/logout`, { withCredentials: true });
  } catch (error) {
    console.log("api/index.js", error.response);
    throw Error("logout error");
  }
}

export { fetchLoginStatus, fetchLogout };