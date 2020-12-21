import Vue from 'vue';
import VueRouter from 'vue-router';
import HomeView from '@/view/HomeView';
import Login from '@/components/Login';
import Register from '@/components/Register';
import UserView from '@/view/UserView';
import ManagerView from '@/view/ManagerView';
import { loginCheck, onlyLoginCheck, logout } from '@/router/common/loginCheck';

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: HomeView,
      beforeEnter: onlyLoginCheck
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/logout',
      beforeEnter: logout
    },
    {
      path: '/user',
      component: UserView,
      beforeEnter: loginCheck
    },
    {
      path: '/manager',
      component: ManagerView,
      beforeEnter: loginCheck
    }
  ]
});