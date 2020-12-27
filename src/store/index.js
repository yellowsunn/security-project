import Vue from 'vue';
import Vuex from 'vuex';
import {
  fetchLoginStatus,
  fetchLogout,
  fetchLogin,
  fetchRegister,
  fetchAdmin
} from '@/api';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    user: {
      name: "",
      role: "",
      isLogin: false
    },
    isLogin: false,
  },
  actions: {
    async FETCH_LOGIN_STATUS() {
      return await fetchLoginStatus();
    },
    async FETCH_LOGOUT() {
      return await fetchLogout();
    },
    async FETCH_LOGIN(context, { account, rememberMe }) {
      return await fetchLogin(account, rememberMe);
    },
    async FETCH_REGISTER(context, account) {
      return await fetchRegister(account);
    },
    async FETCH_ADMIN() {
      return await fetchAdmin();
    },
  },
  mutations: {
    SET_LOGIN_STATUS(state, user) {
      state.user.isLogin = true;
      state.user.name = user.username;
      state.user.role = user.role;
    },
  }
});