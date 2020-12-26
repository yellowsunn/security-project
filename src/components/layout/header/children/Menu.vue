<template>
  <VueSlideToggle v-if="isVisible" :open="toggle" :duration="500">
    <ul class="menu" @click="toggleOff">
      <li class="item"><router-link to="/">HOME</router-link></li>
      <li class="item"><router-link to="/user">USER</router-link></li>
      <li class="item"><router-link to="/manager">MANAGER</router-link></li>
      <li class="item"><router-link to="/admin">ADMIN</router-link></li>
    </ul>
  </VueSlideToggle>
  <ul v-else class="menu">
    <li class="item"><router-link to="/">HOME</router-link></li>
    <li class="item"><router-link to="/user">USER</router-link></li>
    <li class="item"><router-link to="/manager">MANAGER</router-link></li>
    <li class="item"><router-link to="/admin">ADMIN</router-link></li>
  </ul>
</template>

<script>
import { VueSlideToggle } from 'vue-slide-toggle';

export default {
  components: {
    VueSlideToggle
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
    if (innerWidth > 768 && this.isVisible === true) {
      this.isVisible = false;
    } else if (innerWidth <= 768 && this.isVisible === false) {
      this.isVisible = true;
    }
  },
  data() {
    return {
      isVisible: false,
    }
  },
  computed: {
    toggle() {
      return this.$store.state.toggleMenu;
    }
  },
  methods: {
    handleResize() {
      if (innerWidth > 768 && this.isVisible === true) {
        this.isVisible = false;
        this.$store.dispatch('FETCH_TOGGLE_OFF');
      } else if (innerWidth <= 768 && this.isVisible === false) {
        this.isVisible = true;
      }
    },
    toggleOff() {
      this.$store.dispatch('FETCH_TOGGLE_OFF');
    }
  }
};
</script>

<style scoped>
a {
  color: white;
  text-decoration: none;
}

ul {
  margin: 0;
}

.menu .item > a {
  font-size: 0.875rem;
  padding: 0.643em 1.071em;
  border-radius: 0.286em;
}

.menu .item a:hover {
  background: #50555f;
}

.menu .item a {
  display: block;
}

@media screen and (max-width: 48rem) {
  .menu .item {
    text-align: center;
    letter-spacing: 0.188em;
    width: 96vw;
  }

  .menu .item:first-child {
    margin-top: 0.625em;
  }
}
</style>