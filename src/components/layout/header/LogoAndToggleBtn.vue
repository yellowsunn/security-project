<template>
  <div>
    <div class="toggleBtn">
      <a href="#" v-on:click="toggleBtnClick"><i class="fas fa-bars"></i></a>
    </div>
    <div class="logo">
      <div class="image">
        <img src="@/assets/springsecurity.png">
      </div>
      <span class="title">Security</span>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    toggleBtnClick() {
      this.$store.dispatch('FETCH_TOGGLE_CHANGE');
    },
  },
};
</script>

<style scoped>
a {
  color: white;
  text-decoration: none;
}

/* toggle button */
.toggleBtn {
  display: none;
  margin-right: 1rem;
}

.toggleBtn .fa-bars:hover {
  color: #bcbcbc;
}

/* logo */
.logo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  cursor: default;
}

.logo img {
  width: 1.5em;
}

.logo .title {
  color: #65ba40;
}

@media screen and (max-width: 48rem) {
  .toggleBtn {
    display: block;
  }
}

@media screen and (max-width: 30rem) {
  .logo {
    font-size: .75em;
  }
  .toggleBtn {
    margin-right: .75rem;
  }
}

</style>