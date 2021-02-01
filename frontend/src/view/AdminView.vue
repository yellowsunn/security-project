<template>
  <div class="admin" :class="{ 'background' : !forbidden }">
    <Header></Header>
    <AdminContent v-if="!forbidden"></AdminContent>
    <div v-else>
      <Forbidden></Forbidden>
    </div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import AdminContent from '@/components/layout/content/AdminContent';
import Forbidden from '@/components/Forbidden';

export default {
  components: {
    Forbidden,
    Header,
    AdminContent,
  },
  data() {
    return {
      forbidden: false
    }
  },
  created() {
    this.forbidden = this.$store.getters.forbidden;
  }
};
</script>

<style scoped>
.admin {
  width: 100%;
  min-height: 100vh;
}
.background {
  background-color: #eeeeee;
}
</style>