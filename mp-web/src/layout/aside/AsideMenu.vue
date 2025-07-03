<script lang="ts">
import {useStore} from '@/store/index'
import {useBaseStore} from '@/store/base'
import MenuItem from '@/components/interface/MenuItem';
import ModuleItem from "@/components/interface/ModuleItem";

export default {
  name: "AsideMenu",
  props: {
    menuArray: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      store: useStore(),
      baseStore: useBaseStore(),
    }
  },
  methods: {
    menuClick(menu: MenuItem) {
      if (this.baseStore.mobileView) {
        this.store.asideExpand = false;
      }
      if (this.store.tryApplyModule(this.$router, menu.menuId)) {
        return
      }
      this.store.applyModule(this.$router, ModuleItem.fromMenuItem(menu));
    }
  }
}
</script>
<template>
  <template v-for="(menu, index) in menuArray" :key="menu.menuId">
    <!--  目录菜单  -->
    <el-sub-menu v-if="menu.menuType === 1" :index="menu.menuId">
      <template #title>
        <el-icon v-if="menu.iconType === 1 && menu.icon" class="v-lu-menu-icon">
          <component :is="menu.icon"/>
        </el-icon>
        <Icon v-else-if="menu.iconType === 2 && menu.icon" :icon="menu.icon" class="el-icon v-lu-menu-icon"/>
        <span>{{ menu.menuName }}</span>
      </template>
      <AsideMenu v-if="menu.children" :menuArray="menu.children"/>
    </el-sub-menu>
    <!--  菜单项  -->
    <el-menu-item v-else-if="menu.menuType === 2" :index="menu.menuId" @click="menuClick(menu)">
      <el-icon v-if="menu.iconType === 1 && menu.icon" class="v-lu-menu-icon">
        <component :is="menu.icon"/>
      </el-icon>
      <Icon v-else-if="menu.iconType === 2 && menu.icon" :icon="menu.icon" class="el-icon v-lu-menu-icon"/>
      <span>{{ menu.menuName }}</span>
    </el-menu-item>
  </template>
</template>
<style scoped lang="scss">
@import "@/assets/css/base.scss";
</style>
