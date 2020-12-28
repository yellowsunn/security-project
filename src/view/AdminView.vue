<template>
  <div class="admin" :class="{ 'background' : !forbidden }">
    <Header></Header>
    <AdminContent v-if="!forbidden"></AdminContent>
    <div v-else>forbidden</div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import AdminContent from '@/components/layout/content/AdminContent';

export default {
  components: {
    Header,
    AdminContent,
  },
  computed: {
    forbidden() {
      return this.$store.getters.forbidden;
    }
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