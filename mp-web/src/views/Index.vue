<script lang="ts">
import {useStore} from '@/store/index'
import Layout from '@/layout/index.vue'

export default {
  name: "Index",
  components: {
    Layout
  },
  data() {
    return {
      store: useStore(),
    }
  },
  methods: {
    /**
    loadUserInfo() {
      this.$session.get('/api/sys/user/info').then(res => {
        let data = res.data;
        this.store.userInfo = {
          avatar: data.avatar || '',
          nickName: data.nickName || '',
        };
      }).catch(() => {
      })
    }
    **/
  },
  unmounted() {
    this.store.userInfo = {};
    this.store.clearApplyModule();
  }
}
</script>

<template>
  <Layout/>
</template>
