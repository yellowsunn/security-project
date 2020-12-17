import Vue from 'vue';
import Vuex from 'vuex';
import { fetchLoginStatus, fetchLogout } from '@/api';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    isLogin: false,
  },
  actions: {
    async FETCH_LOGIN_STATUS(context) {
      try {
        await fetchLoginStatus();
        context.commit('SET_LOGIN_STATUS');
      } catch (error) {
        console.log("store/index.js", error);
        throw Error("You are not logged in");
      }
    },
    async FETCH_LOGOUT() {
      try {
        await fetchLogout();
      } catch (error) {
        console.log("store/index.js", error);
        throw Error("logout error");
      }
    }
  },
  mutations: {
    SET_LOGIN_STATUS(state) {
      state.isLogin = true;
    }
  }
});