<script>
export default {
  data() {
    return {}
  },
  created() {
    this.url = this.$route.params.url;
  }
}
</script>

<template>
  <iframe :src="url" width="100%" height="100%" frameborder="0" scrolling="yes"></iframe>
</template>
