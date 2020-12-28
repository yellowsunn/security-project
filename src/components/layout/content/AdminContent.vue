<template>
  <section>
    <div class="search_box">
      <i class="fas fa-search"></i>
      <input type="text" placeholder="전체 사용자 검색">
    </div>
    <div class="table_box">
      <div class="title">전체 사용자 <span>{{ data.size }}</span>명</div>
      <ul v-if="!isMobile" class="head">
        <li class="username">아이디</li>
        <li class="password">비밀번호</li>
        <li class="role">권한</li>
        <li class="edit">수정</li>
        <li class="delete">삭제</li>
      </ul>
      <template v-if="!isMobile">
        <UserTable v-for="user in data.users" :user="user" :key="user.username"></UserTable>
      </template>
      <template v-else>
        <UserTableMobile v-for="user in data.users" :user="user" :key="user.username"></UserTableMobile>
      </template>
    </div>
  </section>
</template>

<script>
import UserTable from '@/components/layout/content/children/UserTable';
import UserTableMobile from '@/components/layout/content/children/UserTableMobile';

export default {
  components: {
    UserTable,
    UserTableMobile
  },
  data() {
    const mql = window.matchMedia("screen and (max-width: 768px)");
    return {
      mql,
      isMobile: mql.matches
    }
  },
  computed: {
    data() {
      return this.$store.getters.adminData;
    }
  },
  mounted() {
    this.mql.addEventListener("change", e => {
      this.isMobile = e.matches;
    });
  },
};
</script>

<style lang="scss" scoped>
$normal-padding: 16px;
$head-font-color: #5f5f5f;

ul {
  margin: 0;
}

input {
  border: none;
  &:focus {
    outline: none;
  }
}

section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  .search_box {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 16px;
    border-radius: 4px;
    box-shadow: 0 0 3px 1px #d9d9d9;
    background-color: white;
    margin-bottom: 24px;
    .fa-search {
      margin-right: 16px;
    }
    input {
      width: 100%;
    }
  }
  .table_box {
    width: 100%;
    border-radius: 4px;
    box-shadow: 0 0 3px 1px #d9d9d9;
    background-color: white;
    .title {
      padding: $normal-padding;
      span {
        color: #4383ee;
      }
    }
    ul {
      .username, .role {
        flex: 1 1 25%;
      }
      .password {
        flex: 1 1 40%;
      }
      .edit, .delete {
        flex: 1 1 5%;
        text-align: right;
      }
    }
    .head {
      display: flex;
      padding: $normal-padding;
      color: $head-font-color;
    }
  }
}
</style>