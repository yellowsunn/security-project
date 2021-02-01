import Vue from 'vue';
import Vuex from 'vuex';
import actions from '@/store/actions';
import mutations from '@/store/mutations';
import { Deque } from '@/common/Deque';

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
    boardDto: '',
    postDto: '',
    commentDto: '',

    chatDto: '',
    chatDeque: new Deque(),
    chatSet: new Set(),

    page: 0,
    infiniteId: +new Date() // +는 숫자변환
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