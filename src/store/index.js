import Vue from 'vue';
import Vuex from 'vuex';
import { fetchLogin, fetchLogout, fetchRegister, fetchData } from '@/api';

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
        size: 0,
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
      state.admin.data = data;
    },
  }
});

function hasSessionExpired(data) {
  // 세션이 만료될 경우 서버에서 다음과 같은 문자열 값이 전달된다.
  return data.toString().includes('This session has been expired');
}