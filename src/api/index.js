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
  withCredentials: true,
};

const fetchLogin = async (account, rememberMe) => {
  return await axios.post('/login', account, {
    ...config,
    params: {
      rememberMe,
    },
  });
};

const fetchLogout = async () => {
  return await axios.post('/logout', null, config);
};

const fetchRegister = async (account) => {
  return await axios.post('/register', account, config);
};

const fetchData = async (url) => {
  return await axios.get(url, config);
}

const fetchAdminUpdate = async (data) => {
  return await axios.put('/admin/update', data, config);
}

const fetchAdminDelete = async (data) => {
  return await axios.delete('/admin/delete', {
    ...config,
    data
  });
}

const fetchSearch = async (search, page) => {
  return await axios.get("/admin", {
    ...config,
    params: {
      search, page
    }
  })
}

const fetchBoard = async (title, writer, page) => {
  return await axios.get("/board", {
    ...config,
    params: {
      title, writer, page
    }
  })
}

export {
  fetchLogin,
  fetchLogout,
  fetchRegister,
  fetchData,
  fetchAdminUpdate,
  fetchAdminDelete,
  fetchSearch,
  fetchBoard
};