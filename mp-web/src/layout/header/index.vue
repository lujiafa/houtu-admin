<script lang="ts">
import {useStore} from '@/store/index'
import {useBaseStore} from '@/store/base'
import {useThemeStore} from '@/store/theme'
import Lang from '@/layout/header/Lang.vue'
import Screen from '@/layout/header/Screen.vue'
import ComponentSize from '@/layout/header/ComponentSize.vue'
import UserCenter from '@/layout/header/UserCenter.vue'
import ModuleItem from "@/components/interface/ModuleItem";

export default {
  name: 'header.vue',
  components: {
    Lang,
    Screen,
    ComponentSize,
    UserCenter,
  },
  data() {
    return {
      store: useStore(),
      themeStore: useThemeStore(),
      baseStore: useBaseStore(),
    }
  },
  computed: {
    breadcrumbs() {
      let module: ModuleItem = this.store.currentActiveModule;
      let breadcrumbs = [];
      if (!module || Object.keys(module).length === 0) {
        return breadcrumbs;
      }
      breadcrumbs.push({
        content: module.moduleName,
      });
      if (module.ancestors && module.ancestors.length > 0) {
        module.ancestors.forEach(parent => {
          breadcrumbs.unshift({
            content: parent.moduleName,
          });
        })
      }
      return breadcrumbs;
    },
  },
  methods: {
    asideChange() {
      this.store.asideExpand = !this.store.asideExpand;
    },
  }
}

</script>

<template>
  <div class="v-lu-header-container"
       :style="themeStore.indexHeaderThemeStyle">
    <div class="v-lu-navbar v-lu-left-navbar">
      <div @click="asideChange" class="v-lu-nav-item">
        <el-icon class="v-lu-header-icon">
          <Fold v-if="store.asideExpand"/>
          <Expand v-if="!store.asideExpand"/>
        </el-icon>
      </div>
      <div v-if="!baseStore.mobileView && breadcrumbs.length > 0">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :index="index">
            {{ item.content }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    <div class="v-lu-navbar v-lu-right-navbar">
      <Screen/>
      <ComponentSize/>
      <Lang/>
      <UserCenter/>
    </div>
  </div>
</template>

<style lang="scss" scoped>
@import "@/assets/css/base.scss";

.v-lu-header-container {
  --header-container-background-color: #ffffff;
  --header-container-color: #000000;
  --header-nav-item-hover-background-color: #e9e9eb;
  @include v-lu-fill;
  background-color: var(--header-container-background-color);
  color: var(--header-container-color);
}

.v-lu-navbar {
  height: var(--layout-header-height);
  width: auto;
}

.v-lu-navbar > * {
  height: var(--layout-header-height);
  width: auto;
  display: flex;
  float: left;
  align-items: center;
  padding: 0px 10px;
}

.v-lu-left-navbar {
  float: left;
}

.v-lu-right-navbar {
  float: right;
  padding-right: 10px;
}

.v-lu-nav-item:hover {
  cursor: pointer;
  background-color: var(--header-nav-item-hover-background-color);
}

</style>
<style>
.v-lu-navbar {
  .el-breadcrumb__item > span.el-breadcrumb__separator,
  .el-breadcrumb__item > span.el-breadcrumb__inner,
  .el-breadcrumb__item > span.el-breadcrumb__inner:hover {
    color: inherit;
  }
}
</style>
