<template>
  <ul class="data">
    <li class="username">{{ user.username }}</li>
    <li class="password">
      <input type="password" v-if="edit" v-model="data.password" :placeholder="user.password">
      <span v-else>{{ user.password }}</span>
    </li>
    <li class="role">
      <div v-if="!edit">{{ user.role }}</div>
      <div v-else>
        <select v-model="data.role">
          <option value="ROLE_ADMIN">ROLE_ADMIN</option>
          <option value="ROLE_MANAGER">ROLE_MANAGER</option>
          <option value="ROLE_USER">ROLE_USER</option>
        </select>
      </div>
    </li>
    <li class="edit">
      <i class="fas fa-pen" v-if="!edit" :class="{'root' : isRoot}" @click="editStatus"></i>
      <i class="fas fa-check" v-else @click="fetchUpdate"></i>
    </li>
    <li class="delete"><i class="fas fa-trash-alt" :class="{'root' : isRoot}" @click="fetchDelete"></i></li>
  </ul>
</template>

<script>
export default {
  props: {
    user: Object,
    websocket: WebSocket
  },
  data() {
    return {
      edit: false,
      data: {
        username: this.user.username,
        password: "",
        role: this.user.role,
      },
    }
  },
  computed: {
    isRoot() {
      return this.user.username === 'root';
    }
  },
  methods: {
    async fetchUpdate() {
      if (!(this.data.password === "" && this.user.role === this.data.role)) {
        try {
          await this.$store.dispatch('FETCH_ADMIN_UPDATE', this.data);
          this.websocketUpdate();
        } catch(error) {
          console.log(error);
        }
      }
      this.edit = !this.edit;
    },
    async fetchDelete() {
      try {
        await this.$store.dispatch('FETCH_ADMIN_DELETE', this.data);
        this.websocketDelete();
      } catch (error) {
        console.log(error);
      }
    },
    editStatus() {
      if (this.isRoot) return;
      this.data = {
        ...this.data,
        password: "",
        role: this.user.role
      };
      this.edit = !this.edit;
    },
    websocketUpdate() {
      this.websocket.send(JSON.stringify({
        username: this.data.username,
        isChanged: true,
        isDeleted: false
      }));
    },
    websocketDelete() {
      this.websocket.send(JSON.stringify({
        username: this.data.username,
        isChanged: false,
        isDeleted: true
      }))
    }
  }
};
</script>

<style lang="scss" scoped>
$normal-padding: 16px;
$head-font-color: #5f5f5f;

select {
  font-size: 16px;
  background-color: #fff;
  margin: 0;
  border: 1px solid #aaa;
  border-radius: 1px;
}

.data {
  display: flex;
  padding: $normal-padding;
  border-top: 1px solid #d9d9d9;
  margin: 0;
  .username, .role {
    flex: 1 1 25%;
  }
  .password {
    flex: 1 1 40%;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    padding-right: 64px;
    input {
      width: 100%;
      height: 100%;
      border: 1px solid #aaaaaa;
      padding-left: 4px;
      &:focus {
        outline: none;
      }
    }
  }
  .edit, .delete {
    flex: 1 1 5%;
    text-align: right;
    padding-right: 8px;
    .fas {
      cursor: pointer;
    }
    .root {
      cursor: not-allowed;
    }
  }

}
</style>