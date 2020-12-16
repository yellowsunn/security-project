import Vue from 'vue';
import VueRouter from 'vue-router';
import HomeView from '@/view/HomeView';
import Login from '@/components/Login';
import axios from 'axios';
import UserView from '@/view/UserView';
import ManagerView from '@/view/ManagerView';

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: HomeView
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/user',
      component: UserView,
      beforeEnter: async (to, from, next) => {
        try {
          await axios.get('http://localhost:8080/api', { withCredentials: true });
          next();
        } catch (error) {
          next("/login");
        }
      }
    },
    {
      path: '/manager',
      component: ManagerView,
      beforeEnter: async (to, from, next) => {
        try {
          await axios.get('http://localhost:8080/api', { withCredentials: true });
          next();
        } catch (error) {
          next("/login");
        }
      }
    }
  ]
});