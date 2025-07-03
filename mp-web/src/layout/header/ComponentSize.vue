<script>
import {useBaseStore} from '@/store/base'

export default {
  name: "ComponentSize",
  data() {
    return {
      baseStore: useBaseStore(),
    }
  },
  methods: {
    changeComponentSize(val) {
      this.baseStore.globalComponentSize = val;
    }
  }
}
</script>

<template>
  <div class="v-lu-nav-item" @click="this.$refs.componentSizeDropdown.handleOpen()">
    <el-dropdown class="v-lu-header-icon" trigger="contextmenu" @command="changeComponentSize" ref="componentSizeDropdown">
      <el-icon>
        <Icon icon="mdi:font-size"/>
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="default">{{ $t('componentSize.default') }}</el-dropdown-item>
          <el-dropdown-item command="large">{{ $t('componentSize.large') }}</el-dropdown-item>
          <el-dropdown-item command="small">{{ $t('componentSize.small') }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<style lang="scss" scoped>
@import "@/assets/css/base.scss";
.v-lu-header-icon {
  color: inherit;
}
</style>
