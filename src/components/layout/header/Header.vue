<template>
  <header>
    <LogoAndToggleBtn class="logoAndToggleBtn"></LogoAndToggleBtn>
    <Menu></Menu>
    <UserInfo class="user_info"></UserInfo>
  </header>
</template>

<script>
import Menu from '@/components/layout/header/Menu';
import UserInfo from '@/components/layout/header/UserInfo';
import LogoAndToggleBtn from '@/components/layout/header/LogoAndToggleBtn';

export default {
  components: {
    LogoAndToggleBtn,
    Menu,
    UserInfo,
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

/* menu */
header .menu {
  flex: 1 1 60%;
  display: flex;
  justify-content: space-around;
  margin-bottom: 0;
}

/* user_info */
header .user_info {
  flex: 1 1 20%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

@media screen and (max-width: 48rem) {
  header {
    flex-direction: column;
  }

  header .logoAndToggleBtn {
    font-size: 1.5rem;
    align-self: flex-start;
  }

  header .menu {
    flex-direction: column;
    align-items: center;
    width: 100vw;
  }

  header .user_info {
    position: absolute;
    right: 0.875rem;
  }
}

@media screen and (max-width: 30rem){
  header .user_info {
    top: 0.688em;
  }
}
</style>