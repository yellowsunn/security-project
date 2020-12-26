<template>
  <div class="admin">
    <Header></Header>
    <AdminContent></AdminContent>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import AdminContent from '@/components/layout/content/AdminContent';

export default {
  components: {
    Header,
    AdminContent,
  }
};
</script>

<style scoped>
.admin {
  width: 100vw;
  height: 100vh;
  background-color: #eeeeee;
}
</style>