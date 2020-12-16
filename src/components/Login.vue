<template>
  <section>
    <div class="login_container">
      <b-form class="form" @submit="onSubmit">
        <div class="title">
          <span>Sign in</span>
        </div>
        <div class="info">
          <b-input class="input_box" v-model="account.username" placeholder="user name" :required="true"></b-input>
          <b-input class="input_box" v-model="account.password" type="password" placeholder="password" :required="true"></b-input>
        </div>
        <div class="submit">
          <b-button class="btn btn-lg btn-block submit_btn" type="submit" variant="success">Log in</b-button>
          <div class="error_log">{{error.message}}</div>
        </div>
      </b-form>
    </div>
  </section>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      account: {
        username: '',
        password: '',
      },
      error: {
        message: '',
        status: 200,
      },
    };
  },
  methods: {
    async onSubmit(event) {
      event.preventDefault();

      try {
        const response = await axios.post('http://localhost:8080/api/login', this.account, { withCredentials: true });
        this.error.status = response.status;
        await this.$router.push('/');
      } catch (error) {
        this.error.status = error.response.status;
        this.error.message = error.response.data;
      }

      this.account.username = '';
      this.account.password = '';
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik&display=swap');
section {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f7f7f7;
  font-family: 'Rubik', sans-serif;
  font-size: 1.25rem;
}

section .login_container {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fdfdfd;
  width: 25em;
  height: 26.875em;
  padding: 0 2em;
  border-radius: 0.625em;
  box-shadow: 0.0625em 0.0625em 0.625em 0.0625em #e2e2e2;
}

section .login_container .form {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

section .login_container .form .title {
  font-size: 2.25em;
  text-align: center;
  color: #373c3f;
  flex: 1 1 25%;
  display: flex;
  justify-content: center;
  align-items: center;
}

section .login_container .form .info {
  flex: 1 1 35%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

section .login_container .form .info .input_box {
  background: #e3e9e9;
  height: 3em;
  margin-bottom: 1.25em;
  font-size: 1em;
}

section .login_container .form .submit {
  flex: 1 1 40%;
}

section .login_container .form .submit .submit_btn {
  margin-bottom: 1.25em;
  font-size: 1.125em;
  height: 3em;
}

section .login_container .form .submit .error_log {
  color: #b71c1c;
  margin-left: 0.25em;
  font-style: italic;
}

@media screen and (max-width: 48em) {
  section {
    font-size: 1rem;
  }
}

@media screen and (max-width: 30em) {
  section {
    font-size: 0.8rem;
  }
}
</style>