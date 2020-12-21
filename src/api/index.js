import axios from 'axios';

axios.interceptors.response.use(
  response => {
    return response;
  }, error => {
    return Promise.reject(error.response);
  }
);

const config = {
  baseURL: 'http://localhost:8080/api',
  withCredentials: true
};

const fetchLoginStatus = async () => {
  return await axios.get('', config);
};

const fetchLogout = async () => {
  return await axios.post('/logout', null, config);
}

const fetchLogin = async (account, rememberMe) => {
   return await axios.post(`/login?rememberMe=${rememberMe}`, account, config);
}

export { fetchLoginStatus, fetchLogout, fetchLogin };