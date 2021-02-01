import Vue from 'vue';
import { store } from '@/store';
import VueRouter from 'vue-router';
import HomeView from '@/view/HomeView';
import Login from '@/components/Login';
import Register from '@/components/Register';
import UserView from '@/view/UserView';
import ManagerView from '@/view/ManagerView';
import AdminView from '@/view/AdminView';
import WriteView from '@/view/WriteView';
import PostView from '@/view/PostView';

Vue.use(VueRouter);

export const router = new VueRouter({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: HomeView,
      beforeEnter: fetchData
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
      beforeEnter: async (to, from, next) => {
        try {
          await store.dispatch('FETCH_LOGOUT');
        } catch (error) {
          console.log(error.data);
        }
        next("/");
      }
    },
    {
      path: '/user',
      component: UserView,
      beforeEnter: fetchData
    },
    {
      path: '/user/write',
      component: WriteView,
      beforeEnter: async (to, from, next) => {
        if (to.query.postId !== undefined) {
          await store.dispatch('GET_POST_DATA', to.query.postId);
        }
        next();
      }
    },
    {
      path: '/user/:postId',
      component: PostView,
      beforeEnter: async (to, from, next) => {
        try {
          await store.dispatch('GET_POST_DATA', to.params.postId);
          await store.dispatch('GET_COMMENT_DATA', { postId: to.params.postId });
          await store.dispatch('FETCH_DATA', '/home'); // 로그인한 계정정보를 온다
          next();
        } catch (error) {
          next('/');
        }
      }
    },
    {
      path: '/manager',
      component: ManagerView,
      beforeEnter: async (to, from, next) => {
        await store.dispatch('FETCH_DATA', '/home');
        await store.dispatch('GET_CHAT_DATA');
        await fetchData(to, from, next);
      }
    },
    {
      path: '/admin',
      component: AdminView,
       beforeEnter: async (to, from, next) => {
        await store.dispatch('FETCH_SEARCH');
        await fetchData(to, from, next);
      }
    }
  ]
});

async function fetchData(to, from, next) {
  let path = to.path;
  if (path === '/') path = '/home';

  await store.dispatch('FETCH_DATA', path);

  if (path !== '/home') {
    checkLogin(next);
  } else {
    next();
  }
}

function checkLogin(next) {
  if (store.getters.unauthorized) {
    next('/login');
  } else {
    next();
  }
}