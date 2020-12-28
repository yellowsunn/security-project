<template>
  <div>
    <Header></Header>
    <UserContent v-if="!forbidden"></UserContent>
    <div v-else>forbidden</div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import UserContent from '@/components/layout/content/UserContent';

export default {
  components: {
    Header,
    UserContent
  },
  computed: {
    forbidden() {
      return this.$store.getters.forbidden;
    }
  }
};
</script>

<style scoped>

</style>