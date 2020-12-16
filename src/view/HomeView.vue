<template>
  <div>
    <Header></Header>
    <HomeContent></HomeContent>
  </div>
</template>

<script>
import Header from '@/components/layout/header/Header';
import HomeContent from '@/components/layout/content/HomeContent';

export default {
  components: {
    Header,
    HomeContent
  }
};
</script>

<style scoped>
</style>