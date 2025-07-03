<script lang="ts">
import {useStore} from '@/store/index'

export default {
  name: "main.vue",
  data() {
    return {
      store: useStore(),
    }
  },
  methods: {
    clickTab(ctx: any) {
      this.store.tryApplyModule(this.$router, ctx.paneName);
    },
    // 关闭标签页
    closeTab(moduleId: string) {
      this.store.removeModule(this.$router, moduleId);
    },
    // 关闭右侧标签页
    closeRightTab(module: any) {
      let index = this.store.activatedModules.findIndex((value: any) => value.moduleId === module.moduleId);
      const rightArray = this.store.activatedModules.slice(index + 1);
      rightArray.forEach((value: any) => {
        this.store.removeModule(this.$router, value.moduleId);
      });
    },
    // 关闭左侧标签页
    closeLeftTab(module: any) {
      let index = this.store.activatedModules.findIndex((value: any) => value.moduleId === module.moduleId);
      const leftArray = this.store.activatedModules.slice(0, index);
      leftArray.forEach((value: any) => {
        this.store.removeModule(this.$router, value.moduleId);
      });
    },
    // 关闭其他标签页
    closeOtherTabs(module: any) {
      let index = this.store.activatedModules.findIndex((value: any) => value.moduleId === module.moduleId);
      const otherArray = this.store.activatedModules.filter((_, i) => i !== index);
      otherArray.forEach((value: any) => {
        this.store.removeModule(this.$router, value.moduleId);
      });
    },
  }
}
</script>

<template>
  <el-empty v-if="store.activatedModules.length === 0" description=" " class="v-lu-fill"/>
  <el-tabs
      v-if="store.activatedModules.length > 0"
      v-model="store.currentActiveModule['moduleId']"
      type="card"
      class="v-lu-tabs"
      @tab-click="clickTab"
      @tab-remove="closeTab">
    <el-tab-pane
        v-for="module in store.activatedModules"
        :name="module.moduleId"
        :label="module.moduleName"
        :closable="module.closable"
    >
      <template #label>
        <el-dropdown trigger="contextmenu" >
          <span>{{ module.moduleName }}</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="closeTab(module.moduleId)">{{ $t('common.closeCurrent') }}</el-dropdown-item>
              <el-dropdown-item v-if="module.moduleId !== store.activatedModules[store.activatedModules.length-1].moduleId" @click="closeRightTab(module)">{{ $t('common.closeRight') }}</el-dropdown-item>
              <el-dropdown-item v-if="module.moduleId !== store.activatedModules[0].moduleId" @click="closeLeftTab(module)">{{ $t('common.closeLeft') }}</el-dropdown-item>
              <el-dropdown-item v-if="store.activatedModules.length > 1" @click="closeOtherTabs(module)">{{ $t('common.closeOther') }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>

    </el-tab-pane>
  </el-tabs>
  <div class="v-lu-main-body">
    <router-view v-slot="{ Component }">
      <keep-alive>
        <component :is="Component"/>
      </keep-alive>
    </router-view>
  </div>
</template>

<style scoped lang="scss">
@import "@/assets/css/base.scss";

.el-tabs.v-lu-tabs {
  height: var(--layout-main-tabs-height);
}

.v-lu-main-body {
  @include v-lu-fill-width;
  height: calc(100% - var(--layout-main-tabs-height));
  display: flex;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.v-lu-main-body::-webkit-scrollbar {
  width: 0; /* 隐藏滚动条 */
  height: 0;
}
</style>
<style lang="scss">
@import "@/assets/css/base.scss";

.el-tabs.v-lu-tabs {
  div.el-tabs__header {
    height: var(--layout-main-tabs-height);
    margin-bottom: 0;
    border: none;
    overflow: hidden;
    box-shadow: 0 0px 3px var(--el-text-color-secondary);
  }
}
</style>
