import Vue from 'vue';
import Vuex from 'vuex';
import { fetchLoginStatus, fetchLogout, fetchLogin } from '@/api';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
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
    FETCH_TOGGLE_CHANGE({ commit }) {
      commit('SET_TOGGLE_CHANGE');
    },
    FETCH_TOGGLE_OFF({ commit }) {
      commit('SET_TOGGLE_OFF');
    }
  },
  mutations: {
    SET_LOGIN_STATUS(state) {
      state.isLogin = true;
    },
    SET_TOGGLE_CHANGE(state) {
      state.toggleMenu = !state.toggleMenu;
    },
    SET_TOGGLE_OFF(state) {
      state.toggleMenu = false;
    }
  }
});