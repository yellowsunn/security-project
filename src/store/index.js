import Vue from 'vue';
import Vuex from 'vuex';
import {
  fetchLogin,
  fetchLogout,
  fetchRegister,
  fetchData,
  fetchAdminUpdate,
  fetchAdminDelete,
  fetchSearch
} from '@/api';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    userInfo: {
      username: "",
      role: "",
    },
    admin: {
      data: {
        users: [],
        totalSize: 0,
        lastPage: false
      }
    },
    httpStatus: 500,
  },
  getters: {
    unauthorized(state) {
      return state.httpStatus === 401;
    },
    forbidden(state) {
      return state.httpStatus === 403;
    },
    adminData(state) {
      return state.admin.data;
    }
  },
  actions: {
    async FETCH_LOGIN(context, { account, rememberMe }) {
      return await fetchLogin(account, rememberMe);
    },
    async FETCH_LOGOUT() {
      return await fetchLogout();
    },
    async FETCH_REGISTER(context, account) {
      return await fetchRegister(account);
    },
    async FETCH_DATA({ commit }, url) {
      try {
        const response = await fetchData(url);

        // 세션이 만료되었는지 확인
        if (hasSessionExpired(response.data)) {
          commit('SESSION_EXPIRED');
          return;
        }

        commit('SET_HTTP_STATUS', response.status);

        if (url === '/home') {
          commit('SET_USER_INFO', response.data);
        } else if (url === '/admin') {
          commit('SET_ADMIN_DATA', response.data);
        }
      } catch (error) {
        commit('SET_HTTP_STATUS', error.status)
        console.log(error);
      }
    },
    async FETCH_ADMIN_UPDATE(context, data) {
      return await fetchAdminUpdate(data);
    },
    async FETCH_ADMIN_DELETE(context, data) {
      return await fetchAdminDelete(data);
    },
    async FETCH_SEARCH({ commit }, search) {
      try {
        const response = await fetchSearch(search);
        commit('SET_ADMIN_DATA', response.data);
      } catch (error) {
        console.log(error);
      }
    },
    async FETCH_SEARCH_SCROLL({ commit }, { search, page }) {
      try {
        const response = await fetchSearch(search, page);
        commit('ADD_ADMIN_DATA', response.data);
        return response;
      } catch (error) {
        console.log(error);
      }
    }
  },
  mutations: {
    SET_USER_INFO(state, user) {
      state.userInfo = user;
    },
    SET_HTTP_STATUS(state, status) {
      state.httpStatus = status;
    },
    SESSION_EXPIRED(state) {
      state.httpStatus = 401;
    },
    SET_ADMIN_DATA(state, data) {
      state.admin.data = {
        users: data.content,
        totalSize: data.totalElements,
        lastPage: data.last,
      };
    },
    ADD_ADMIN_DATA(state, data) {
      const adminData = state.admin.data;
      adminData.users.push(...data.content);

      // 중복 제거
      let set = new Set(adminData.users.map(JSON.stringify));
      adminData.users = Array.from(set).map(JSON.parse);

      adminData.totalSize = data.totalElements;
      adminData.lastPage = data.last;
    }
  }
});

function hasSessionExpired(data) {
  // 세션이 만료될 경우 서버에서 다음과 같은 문자열 값이 전달된다.
  return data.toString().includes('This session has been expired');
}