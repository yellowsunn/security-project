<template>
  <section>
    <div class="search_box">
      <i class="fas fa-search"></i>
      <input type="text" v-model="search" v-debounce:300ms="fetchSearch" placeholder="전체 사용자 검색">
    </div>
    <div class="table_box">
      <div class="title">전체 사용자 <span>{{ adminData.totalSize }}</span>명</div>
      <ul v-if="!isMobile" class="head">
        <li class="username">아이디</li>
        <li class="password">비밀번호</li>
        <li class="role">권한</li>
        <li class="edit">수정</li>
        <li class="delete">삭제</li>
      </ul>
      <template v-if="!isMobile">
        <UserTable v-for="user in adminData.users" :user="user" :websocket="websocket" :key="user.username"></UserTable>
        <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral">
          <span slot="no-more"></span>
          <span slot="no-results"></span>
        </infinite-loading>
      </template>
      <template v-else>
        <UserTableMobile v-for="user in adminData.users" :user="user" :websocket="websocket" :key="user.username"></UserTableMobile>
        <infinite-loading :identifier="infiniteId" @infinite="infiniteHandler" spinner="spiral">
          <span slot="no-more"></span>
          <span slot="no-results"></span>
        </infinite-loading>
      </template>
    </div>

  </section>
</template>

<script>
import UserTable from '@/components/layout/content/children/UserTable';
import UserTableMobile from '@/components/layout/content/children/UserTableMobile';
import { getWebSocket } from '@/api';
export default {
  components: {
    UserTable,
    UserTableMobile
  },
  data() {
    const mql = window.matchMedia("screen and (max-width: 768px)");
    const websocket = getWebSocket('/websocket');
    return {
      mql,
      isMobile: mql.matches,
      websocket,
      search: "",
      page: 0,
      infiniteId: +new Date() // +는 숫자변환
    }
  },
  computed: {
    adminData() {
      return this.$store.getters.adminData;
    }
  },
  mounted() {
    this.mql.addEventListener("change", e => {
      this.isMobile = e.matches;
    });
    this.initWebsocket();
  },
  beforeDestroy() {
    this.websocket.close();
  },
  methods: {
    initWebsocket() {
      this.websocket.onopen = () => {
        console.log("Websocket is connected.");
      }
      this.websocket.onclose = () => {
        console.log("Websocket is closed.");
      }
      this.websocket.onmessage = evt => {
        const responseData = JSON.parse(evt.data);
        const findUser = this.$store.state.admin.data.users.find(element => {
          if (element.username === responseData.username) {
            return true;
          }
        });
        console.log(responseData);

        if (findUser === undefined) return;
        if (responseData.isChanged) {
          findUser.password = responseData.password;
          findUser.role = responseData.role;
        } else if (responseData.isDeleted) {
          const adminData = this.$store.state.admin.data;
          const users = adminData.users;
          const deletedUserIdx = users.indexOf(findUser);
          users.splice(deletedUserIdx, 1);
          adminData.totalSize -= 1;
          // 삭제한 유저 위치 페이지로 다시 설정
          this.page = parseInt(deletedUserIdx / 10);
          this.infiniteId += 1;
        }
      }
      this.websocket.onerror = evt => {
        console.warn("ERROR: " + evt.data);
      }
    },
    fetchSearch() {
      this.$store.dispatch('FETCH_SEARCH', this.search);
      this.page = 0;
      this.infiniteId += 1;
    },
    async infiniteHandler($state) {
      try {
        await this.$store.dispatch('FETCH_SEARCH_SCROLL', { search: this.search, page: this.page });

        if (this.adminData.lastPage) {
          console.log(this.adminData.users.length, this.adminData.totalSize);
          $state.complete();
        } else {
          this.page += 1;
          $state.loaded();
        }
      } catch (error) {
        console.log(error);
      }
    }
  }
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