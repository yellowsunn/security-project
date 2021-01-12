import Vue from 'vue';
import Vuex from 'vuex';
import actions from '@/store/actions';
import mutations from '@/store/mutations';

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
    boardDto: ''
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
    },
    boardData(state) {
      return state.boardDto;
    }
  },
  actions,
  mutations,
});