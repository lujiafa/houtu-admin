<script>
import Header from '@/layout/header/index.vue';
import Aside from '@/layout/aside/index.vue';
import Main from '@/layout/main/index.vue';

export default {
  name: 'Layout.vue',
  components: {
    Header,
    Aside,
    Main
  }
}
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧栏 -->
    <el-aside class="layout-aside">
      <Aside/>
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <Header/>
      </el-header>
      <el-main class="layout-main">
        <Main/>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
@use '@/assets/css/base.scss' as *;

section.el-container.layout-container {
  height: 100%;
}

.layout-header {
  @include v-lu-fill-width;
  height: var(--layout-header-height);
  box-shadow: 0 0px 2px var(--el-text-color-secondary);
}

.layout-aside {
  width: auto;
  overflow: hidden;
}

.layout-main {
  @include v-lu-fill-width;
  overflow: hidden;
}
</style>
