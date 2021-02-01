<template>
  <div>
    <Header></Header>
    <ManagerContent v-if="!forbidden"></ManagerContent>
    <div v-else>
      <Forbidden></Forbidden>
    </div>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import ManagerContent from '@/components/layout/content/ManagerContent';
import Forbidden from '@/components/Forbidden';

export default {
  components: {
    Forbidden,
    Header,
    ManagerContent
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

</style>