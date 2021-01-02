import Vue from 'vue';
import App from './App.vue';
import BootstrapVue from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import { router } from './router/index';
import { store } from './store/index'
import VueDebounce from 'vue-debounce';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(VueDebounce);

new Vue({
  render: h => h(App),
  router,
  store,
}).$mount('#app')
