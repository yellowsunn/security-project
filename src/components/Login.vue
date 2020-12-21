<template>
  <section>
    <div class="login_container">
      <b-form class="form" @submit="onSubmit">
        <div class="title">
          <span>Log in</span>
        </div>
        <div class="info">
          <b-input class="input_box" v-model="account.username" placeholder="user name" :required="true"></b-input>
          <b-input class="input_box" v-model="account.password" type="password" placeholder="password"
                   :required="true"></b-input>
          <div class="check_box">
            <input type="checkbox" id="checkbox" v-model="rememberMe">
            <label for="checkbox">로그인 상태 유지</label>
          </div>
        </div>
        <b-button class="btn btn-lg btn-block submit_btn" type="submit" variant="success">Log in</b-button>
      </b-form>
      <div class="etc">
        <div class="register">Don't have an account? <a href="/register">Register now</a></div>
        <div class="error_log">{{ error.message }}</div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      account: {
        username: '',
        password: '',
      },
      error: {
        message: '',
      },
      rememberMe: false,
    };
  },
  methods: {
    async onSubmit(event) {
      event.preventDefault();
      try {
        await this.$store.dispatch(
            'FETCH_LOGIN',
            { account: this.account, rememberMe: this.rememberMe },
        );
        await this.$router.push('/');
      } catch (error) {
        this.error.message = error.data;
        this.account.username = '';
        this.account.password = '';
        console.log(error.data);
      }
    }
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik&display=swap');

label {
  margin: 0;
}

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
  flex-direction: column;
  background: #fdfdfd;
  width: 25em;
  height: 26.875em;
  padding: 0 2em;
  border-radius: 0.625em;
  box-shadow: 0.0625em 0.0625em 0.625em 0.0625em #e2e2e2;
}

section .login_container .form {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

section .login_container .form .title {
  font-size: 2.25em;
  text-align: center;
  color: #373c3f;
  margin: .622em;
  display: flex;
  justify-content: center;
  align-items: center;
}

section .login_container .form .info {
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

section .login_container .form .info .check_box {
  display: flex;
  align-items: center;
  margin-bottom: 1.25em;
  font-size: 0.8em;
  color: #4e555b;
  text-align: center;
}

section .login_container .form .info .check_box #checkbox {
  zoom: 1.5;
  margin-right: 0.5em;
}

section .login_container .form .submit_btn {
  margin-bottom: 1.25em;
  font-size: 1.125em;
  height: 3em;
}

section .login_container .etc .register {
  color: #8e8e8e;
  font-size: 0.8em;
  margin-bottom: 0.6em;
}

section .login_container .etc .error_log {
  color: #b71c1c;
  margin-left: 0.25em;
  font-style: italic;
}

@media screen and (max-width: 48em) {
  section {
    font-size: 1rem;
  }

  section .login_container .form .info .check_box #checkbox {
    zoom: 1.2;
  }
}

@media screen and (max-width: 30em) {
  section {
    font-size: 0.8rem;
  }

  section .login_container .form .info .check_box #checkbox {
    zoom: 0.96;
  }
}
</style>