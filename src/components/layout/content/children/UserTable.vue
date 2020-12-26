<template>
  <ul class="data">
    <li class="username">admin</li>
    <li class="password">{bcrypt}$2a$10$5ZtFuEFw2i6VkUsDgA5DT.IPnuQZM1RP3zEKk5vQm6ONqms/pGv66	</li>
    <li class="role">
      <div v-if="!edit">ROLE_ADMIN</div>
      <div v-else>
        <select v-model="selected">
          <option value="ROLE_ADMIN">ROLE_ADMIN</option>
          <option value="ROLE_MANAGER">ROLE_MANAGER</option>
          <option value="ROLE_USER">ROLE_USER</option>
        </select>
      </div>
    </li>
    <li class="edit"><i class="fas" :class="edit ? 'fa-check' : 'fa-pen'" @click="edit = !edit"></i></li>
    <li class="delete"><i class="fas fa-trash-alt"></i></li>
  </ul>
</template>

<script>
export default {
  data() {
    return {
      edit: false,
      selected: "ROLE_MANAGER"
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
    overflow: hidden;
    padding-right: 64px;
  }
  .edit, .delete {
    flex: 1 1 5%;
    text-align: right;
    padding-right: 8px;
    .fas {
      cursor: pointer;
    }
  }
}
</style>