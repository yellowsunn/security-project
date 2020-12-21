<template>
  <div>
    {{ message }}
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      message: ""
    }
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get("http://localhost:8080/api/user", {withCredentials: true});
        this.message = response.data;
        console.log(response);
      } catch (error) {
        console.log(error);
        this.message = error.data;
      }
    }
  }
};
</script>

<style scoped>

</style>