<template>
  <header>
    <div class="logoAndToggleBtn">
      <div class="toggleBtn">
        <a href="#" v-on:click="toggleBtnClick"><i class="fas fa-bars" v-bind:class="{ 'visible' : isVisible }"></i></a>
      </div>
      <div class="logo" v-on:click="$router.push('/')">
        <div class="image">
          <img src="@/assets/springsecurity.png">
        </div>
        <span class="title">Security</span>
      </div>
    </div>
    <ul class="menu" v-bind:class="{ 'visible' : isVisible }">
      <li class="item"><a href="/">HOME</a></li>
      <li class="item"><a href="/user">USER</a></li>
      <li class="item"><a href="/manager">MANAGER</a></li>
      <li class="item"><a href="#">ADMIN</a></li>
    </ul>
    <UserInfo></UserInfo>
    <div class="user_info">
      <a href="/login" class="login">로그인</a>
    </div>
  </header>
</template>

<script>
import UserInfo from '@/components/layout/header/UserInfo';

export default {
  components: {
    UserInfo
  },
  mounted() {
    window.addEventListener('resize', this.handleResize);
  },
  data() {
    return {
      isVisible: false,
    };
  },
  methods: {
    toggleBtnClick() {
      this.isVisible = !this.isVisible;
    },
    handleResize() {
      if (innerWidth > 768 && this.isVisible === true) {
        console.log("호출");
        this.isVisible = false;
      }
    }
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap');

header {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  background: var(--header-color);
  justify-content: space-between;
  align-items: center;
  padding: 0.6em 1em;
  color: white;
}

a {
  color: white;
  text-decoration: none;
}

/* logoAndToggleBtn */
header .logoAndToggleBtn {
  flex: 1 1 20%;
  font-size: 1.8rem;
  display: flex;
}

  /* toggle button */
header .logoAndToggleBtn .toggleBtn {
  display: none;
  margin-right: 16px;
}

header .logoAndToggleBtn .toggleBtn .fa-bars:hover {
  color: #bcbcbc;
}

  /* logo */
header .logoAndToggleBtn .logo {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  cursor: pointer;
}

header .logoAndToggleBtn .logo img {
  width: 1.5em;
}

header .logoAndToggleBtn .logo .title {
  color: #65ba40;
}

/* menu */
header .menu {
  flex: 1 1 60%;
  display: flex;
  justify-content: space-around;
  margin-bottom: 0;
}

header .menu .item > a {
  font-size: 14px;
  padding: 9px 15px;
  border-radius: 4px;
}

header .menu .item a:hover {
  background: #50555f;
}

header .menu .item a {
  display: block;
}

/* user_info */
header .user_info {
  flex: 1 1 20%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

header .user_info .login {
  background-color: #1a72e6;
  color: white;
  padding: 0.563em 0.938em;
  border-radius: 4px;
}

header .user_info .login:hover {
  background-color: #4383ee;
}

@media screen and (max-width: 768px) {
  header {
    flex-direction: column;
  }

  header .logoAndToggleBtn {
    font-size: 1.5rem;
    align-self: flex-start;
  }

  header .logoAndToggleBtn .toggleBtn {
    display: block;
  }

  header .menu {
    flex-direction: column;
    align-items: center;
    width: 100vw;
    display: none;
  }

  header .menu .item {
    text-align: center;
    letter-spacing: 3px;
    width: 96vw;
  }

  header .menu .item:first-child {
    margin-top: 0.6rem;
  }

  header .user_info {
    position: absolute;
    right: 14px;
  }

  header .user_info .login {
    font-size: 0.9rem;
  }

  /*클릭 이벤트*/
  header > .visible {
    display: flex;
  }
}
</style>