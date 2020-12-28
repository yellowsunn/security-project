<template>
  <div>
    <Header></Header>
    <ManagerContent v-if="!forbidden"></ManagerContent>
    <div v-else>forbidden</div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import ManagerContent from '@/components/layout/content/ManagerContent';
export default {
  components: {
    Header,
    ManagerContent
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