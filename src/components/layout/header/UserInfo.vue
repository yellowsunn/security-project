<template>
  <div>
    <a v-show="!loginStatus" href="/login" class="login">Login</a>
    <a v-show="!loginStatus" href="/register" class="register">Register</a>
    <a v-show="loginStatus" href="/logout" class="logout">Sign out</a>
  </div>
</template>

<script>
export default {
  computed: {
    loginStatus() {
      return this.$store.state.isLogin;
    }
  },
};
</script>

<style scoped>
a {
  text-decoration: none;
}

.login {
  background-color: #1a72e6;
  color: white;
  padding: 0.563em 1.5em;
  border-radius: .25em;
  margin-right: 1em;
}

.register {
  background-color: white;
  padding: 0.542em 0.938em;
  color: var(--button-color);
  border: .0625em solid var(--button-color);
  border-radius: .25em;
}

.logout {
  background-color: var(--button-color);
  color: white;
  padding: 0.563em 0.938em;
  border-radius: .25em;
}

.login:hover, .logout:hover {
  background-color: #4383ee;
}

.register:hover {
  background-color: #e3ebff;
}

@media screen and (max-width: 48rem) {
  .login, .register, .logout {
    font-size: 0.9rem;
  }
}

@media screen and (max-width: 30rem) {
  .login, .register, .logout {
    font-size: 0.75em;
  }

}
</style>