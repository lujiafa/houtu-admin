<script lang="ts">
import {useBaseStore} from '@/store/base'
import {useThemeStore} from '@/store/theme';
import {useLocaleStore} from '@/store/locale';

import Layout from '@/layout/index.vue';

import {ElConfigProvider} from 'element-plus';

export default {
  name: 'App',
  components: {
    Layout,
    ElConfigProvider
  },
  data() {
    return {
      baseStore: useBaseStore(),
      themeStore: useThemeStore(),
      localeStore: useLocaleStore(),
    }
  },
  methods: {
    /**
     * 初始化获取系统名称信息
     */
    initSystemName() {
      let systemName = import.meta.env.VITE_APP_NAME;
      if (!systemName || !systemName.length) {
        systemName = this.$t('systemName');
      }
      this.baseStore.systemName = systemName;
      document.title = systemName;
    },
    // 窗口大小变化监听
    resizeWindow() {
      if (window.innerWidth < 600) {
        this.baseStore.mobileView = true;
      } else {
        this.baseStore.mobileView = false;
      }
    },
    /**
     * 解析初始化路由
     */
    parseInitRouteState() {
      let initRoutePath = window.location.hash.slice(1);
      if (initRoutePath && initRoutePath.length > 0 && "/" !== initRoutePath) {
        this.$router.push(initRoutePath);
      } else {
        this.$router.push('/login');
      }
    }
  },
  created() {
    this.initSystemName();
    this.localeStore.initLocale(undefined, this.$i18n);

    this.themeStore.initTheme();

    // 初始化窗口大小相关处理
    this.resizeWindow();
    // 添加窗口大小监听事件
    window.addEventListener('resize', this.resizeWindow);

    this.parseInitRouteState();
  },
  unmounted() {
    // 卸载窗口大小监听事件
    window.removeEventListener('resize', this.resizeWindow);
  }
}
</script>

<template>
  <el-config-provider :locale="localeStore.elementPlusLocale" :size="baseStore.globalComponentSize">
    <router-view/>
  </el-config-provider>
</template>
