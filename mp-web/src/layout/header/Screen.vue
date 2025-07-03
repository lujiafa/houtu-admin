<script>
import screenfull from 'screenfull';

export default {
  name: "screen",
  data() {
    return {
      // 是否全屏
      fullScreen: false,
    }
  },
  methods: {
    // 窗口大小变更事件监听
    screenChangeEvent(event) {
      this.fullScreen = screenfull.isFullscreen;
    },
    toggleFullscreen() {
      if (screenfull.isEnabled) {
        screenfull.toggle();
      }
    }
  },
  mounted() {
    // 添加Esc事件监听
    document.addEventListener('fullscreenchange', this.screenChangeEvent);
  },
  beforeUnmount() {
    // 卸载Esc事件监听
    document.removeEventListener('fullscreenchange', this.screenChangeEvent);
  }
}
</script>

<template>
  <div class="v-lu-header-icon v-lu-nav-item" @click="toggleFullscreen">
    <el-icon>
      <Icon v-if="fullScreen" icon="charm:screen-minimise"/>
      <Icon v-if="!fullScreen" icon="charm:screen-maximise"/>
    </el-icon>
  </div>
</template>
