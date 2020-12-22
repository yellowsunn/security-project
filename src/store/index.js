import Vue from 'vue';
import Vuex from 'vuex';
import {
  fetchLoginStatus,
  fetchLogout,
  fetchLogin,
  fetchRegister,
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
    toggleMenu: false
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
    FETCH_TOGGLE_CHANGE({ commit }) {
      commit('SET_TOGGLE_CHANGE');
    },
    FETCH_TOGGLE_OFF({ commit }) {
      commit('SET_TOGGLE_OFF');
    }
  },
  mutations: {
    SET_LOGIN_STATUS(state, user) {
      state.user.isLogin = true;
      state.user.name = user.username;
      state.user.role = user.role;
    },
    SET_TOGGLE_CHANGE(state) {
      state.toggleMenu = !state.toggleMenu;
    },
    SET_TOGGLE_OFF(state) {
      state.toggleMenu = false;
    }
  }
});