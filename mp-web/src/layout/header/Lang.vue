<script lang="ts">
import {useThemeStore} from '@/store/theme';
import {useLocaleStore} from '@/store/locale';

export default {
  name: "lang",
  data() {
    return {
      themeStore: useThemeStore(),
      localeStore: useLocaleStore()
    }
  },
  methods: {
    changeLang(item) {
      this.localeStore.applyLocale(item.code, this.$i18n);
    }
  },
}
</script>

<template>
  <div class="v-lu-nav-item" @click="this.$refs.langDropdown.handleOpen()">
    <el-dropdown class="v-lu-header-icon" trigger="contextmenu" @command="changeLang" ref="langDropdown">
      <el-icon>
        <Icon icon="ri:translate"/>
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="(item, index) in localeStore.supportedLocales" :command="item">
            {{ item.label }}
          </el-dropdown-item>
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
