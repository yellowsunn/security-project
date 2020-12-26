<template>
  <div class="label_and_data">
    <div class="label" @click="changeToggle">
      <div>
        <i class="fas fa-user-circle"></i>
        <span>admin</span>
      </div>
      <div>
        <i class="fas" :class="toggle ? 'fa-minus' : 'fa-plus'"></i>
      </div>
    </div>
    <VueSlideToggle class="data" :open="toggle">
      <ul>
        <li class="username">
          <span>아이디: </span>
          <div>admin</div>
        </li>
        <li class="password">
          <span>비밀번호: </span>
          <div id="password">{bcrypt}$2a$10$5ZtFuEFw2i6VkUsDgA5DT.IPnuQZM1RP3zEKk5vQm6ONqms/pGv66</div>
        </li>
        <li class="role">
          <span>권한: </span>
          <div v-if="!edit">ROLE_ADMIN</div>
          <div v-else>
            <select v-model="selected">
              <option value="ROLE_ADMIN">ROLE_ADMIN</option>
              <option value="ROLE_MANAGER">ROLE_MANAGER</option>
              <option value="ROLE_USER">ROLE_USER</option>
            </select>
          </div>
        </li>
      </ul>
      <div class="buttons">
        <div class="edit" @click="edit = !edit" v-text="edit ? '적용' : '수정'"></div>
        <div class="delete">삭제</div>
      </div>
    </VueSlideToggle>
  </div>
</template>

<script>
import { VueSlideToggle } from 'vue-slide-toggle';

export default {
  components: {
    VueSlideToggle
  },
  data() {
    return {
      toggle: false,
      edit: false,
      selected: "ROLE_MANAGER"
    }
  },
  methods: {
    changeToggle() {
      this.toggle= !this.toggle;
    },
  }
};
</script>

<style lang="scss" scoped>
$normal-padding: 16px;
$head-font-color: #5f5f5f;

ul {
  margin: 0;
}

select {
  font-size: 16px;
  background-color: #fff;
  margin: 0;
  border: 1px solid #aaa;
  border-radius: 1px;
}

.label_and_data {
  display: flex;
  flex-direction: column;
  .label {
    cursor: pointer;
    border-top: 1px solid #d9d9d9;
    display: flex;
    justify-content: space-between;
    padding: $normal-padding;
    .fa-user-circle {
      margin-right: 6px;
    }
  }
  .data {
    ul {
      li {
        display: flex;
        white-space: nowrap;
        padding: 4px 16px;
        span {
          color: $head-font-color;
          flex: 1 1 30%;
        }
        div {
          text-overflow: ellipsis;
          overflow: hidden;
          padding-right: 10%;
          flex: 1 1 70%;
        }
      }
    }
    .buttons {
      display: flex;
      .edit, .delete {
        flex: 1 1 50%;
        text-align: center;
        padding: 4px 0;
        border-radius: 4px;
        color: white;
      }
      .edit {
        cursor: pointer;
        background-color: #38b97b;
        margin: 8px 8px 16px 16px;
        &:hover {
          background-color: #5ac492;
        }
      }
      .delete {
        cursor: pointer;
        background-color: #f14541;
        margin: 8px 16px 16px 8px;
        &:hover {
          background-color: #ee615f;
        }
      }
    }
  }
}
</style>