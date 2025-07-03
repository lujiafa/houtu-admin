<script lang="ts">
import {useStore} from '@/store/index'
import {useBaseStore} from '@/store/base'
import {useThemeStore} from '@/store/theme'
import AsideMenu from '@/layout/aside/AsideMenu.vue'
import MenuItem from '@/components/interface/MenuItem';
import ModuleItem from "@/components/interface/ModuleItem";

export default {
  name: "aside.vue",
  components: {
    AsideMenu,
  },
  data() {
    return {
      store: useStore(),
      themeStore: useThemeStore(),
      baseStore: useBaseStore(),
      logoImg: import.meta.env.VITE_APP_LOGO || 'src/assets/default-logo.svg',
      menus: [],
    }
  },
  methods: {
    logoClick() {
      this.$router.push('/index');
    },
    /**
     * 加载菜单数据
     * @param initRoutePath 初始化匹配路径
     */
    loadMenuData(initRoutePath: string) {
      this.$session.get('/api/menu/mylist').then(res => {
        let menuRecords = res.data;
        if (this.store.enableHome) {
          this.menus = [this.store.homeMenu, ...MenuItem.fromMenuRecords(menuRecords, null)];
        } else {
          this.menus = MenuItem.fromMenuRecords(menuRecords, null);
        }
        if (initRoutePath && initRoutePath.length > 0) {
          let menuItem = MenuItem.findMenuItemByPath(initRoutePath, this.menus);
          if (menuItem) {
            this.store.applyModule(this.$router, ModuleItem.fromMenuItem(menuItem));
          }
        }
      }).catch(() => {
      })
    },
  },
  created() {
    // 启用首页时默认打开
    if (this.store.enableHome) {
      this.store.applyModule(this.$router, ModuleItem.fromMenuItem(this.store.homeMenu));
    }

    let initPath = window.location.hash.slice(1);
    this.loadMenuData(initPath)
  }
}
</script>

<template>
  <!-- 普通容器式 -->
  <div v-if="!baseStore.mobileView" class="v-lu-logo-container"
       :class="store.asideExpand ? 'v-lu-aside-expand' : 'v-lu-aside-collapse'" @click="logoClick"
       :style="themeStore.indexAsideLogoThemeStyle">
    <img v-if="logoImg" :src="logoImg" @error="logoImg=false" height="36px"/>
    <span v-if="store.asideExpand" class="v-lu-logo-text">{{ baseStore.systemName }}</span>
  </div>
  <div v-if="!baseStore.mobileView" class="v-lu-menu-container"
       :class="store.asideExpand ? 'v-lu-aside-expand' : 'v-lu-aside-collapse'">
    <!--  功能主页存在标签栏切换，这里未使用"el-menu"自带"router/index"功能  -->
    <el-menu
        class="v-lu-menu"
        :collapse="!store.asideExpand"
        :default-active="store.currentActiveModule?store.currentActiveModule['menuId']:''"
    >
      <AsideMenu :menuArray="menus"/>
    </el-menu>
  </div>

  <!-- 抽屉式（移动设备） -->
  <el-drawer v-if="baseStore.mobileView"
             v-model="store.asideExpand"
             direction="ltr"
             size="var(--layout-aside-expand-width)"
             :with-header="false"
             :show-close="false"
             :close-on-click-modal="true"
             @close-auto-focus="store.asideExpand=false"
             class="v-lu-aside-drawer">
    <div class="v-lu-logo-container" @click="logoClick" :style="themeStore.indexAsideLogoThemeStyle">
      <img v-if="logoImg" :src="logoImg" @error="logoImg=false" height="36px"/>
      <span v-if="store.asideExpand" class="v-lu-logo-text">{{ baseStore.systemName }}</span>
    </div>
    <div class="v-lu-menu-container">
      <!--  功能主页存在标签栏切换，这里未使用"el-menu"自带"router/index"功能  -->
      <el-menu
          class="v-lu-menu"
          :collapse="false"
          :default-active="store.currentActiveModule?store.currentActiveModule['menuId']:''"
      >
        <AsideMenu :menuArray="menus"/>
      </el-menu>
    </div>
  </el-drawer>
</template>

<style scoped lang="scss">
@import "@/assets/css/base.scss";

.v-lu-logo-container {
  --aside-logo-container-background-color: #001529;
  --aside-logo-container-color: #ffffff;
  background-color: var(--aside-logo-container-background-color);
  color: var(--aside-logo-container-color);
}

.v-lu-menu-container {
  background-color: var(--el-menu-bg-color);
}

.v-lu-aside-expand {
  width: var(--layout-aside-expand-width);
}

.v-lu-aside-collapse {
  width: auto;
}

.v-lu-logo-container {
  height: var(--layout-aside-logo-height);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;

  .v-lu-logo-text {
    font-weight: 700;
    color: #ffffff;
    margin-left: 5px;
  }
}

.v-lu-menu-container {
  height: calc(100% - var(--layout-aside-logo-height));
  overflow-y: scroll;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */

  .el-menu.v-lu-menu {
    border: none;
  }
}

.v-lu-menu-container::-webkit-scrollbar {
  width: 0; /* 隐藏滚动条 */
  height: 0;
}
</style>
<style lang="scss">
:root {
  --el-menu-bg-color: #001529;
  --el-menu-text-color: #ffffff;
  --el-menu-active-color: #409EFF;
  --el-menu-hover-bg-color: #223F7B;
}

.v-lu-aside-drawer .el-drawer__body {
  padding: 0px;
}
</style>
