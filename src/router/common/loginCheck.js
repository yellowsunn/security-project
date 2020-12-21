import { store } from '@/store';

const onlyLoginCheck = async (to, from, next) => {
  try {
    await store.dispatch('FETCH_LOGIN_STATUS');
    store.commit('SET_LOGIN_STATUS');
  } catch (error) {
    console.log(error.data);
  }
  next();
}

// 로그인을 안한 경우 로그인화면으로 보냄
const loginCheck = async (to, from, next) => {
  try {
    await store.dispatch('FETCH_LOGIN_STATUS');
    store.commit('SET_LOGIN_STATUS');
    next();
  } catch (error) {
    console.log(error.data);
    next("/login");
  }
}

const logout = async (to, from, next) => {
  try {
    await store.dispatch('FETCH_LOGOUT');
  } catch (error) {
    console.log(error.data);
  }
  next("/");
}

export {
  onlyLoginCheck,
  loginCheck,
  logout
}