<script lang="ts">
import {useStore} from '@/store/index'
import {themes, useThemeStore} from '@/store/theme'
import ModuleItem from "@/components/interface/ModuleItem"
// import Theme from "@/layout/header/Theme.vue"

export default {
  name: "lang",
  data() {
    return {
      store: useStore(),
      themeStore: useThemeStore(),
      // 是否展示 开启MFA 功能
      showOpenMFA: false,
      // MFA中OTP是否已启用
      mfaOTPEnabled: false,
      // 是否显示切换主题面板
      visibleSwitchTheme: false,
    }
  },
  methods: {
    themes() {
      return themes
    },
    // 开启MFA
    openMFA() {
      let that = this;
      this.$confirm(this.$i18n.t('common.openMFADesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        that.$session.post("/api/mfa/open").then(res => {
          that.$message({
            type: 'success',
            message: that.$i18n.t('common.operateSuccess')
          });
          that.loadMFAState(true);
        }).catch(() => {
        });
      });
    },
    // 显示“我的OTP”
    displayMyOTP() {
      // 打开MFA功能
      this.store.applyModule(this.$router, new ModuleItem("my-otp", this.$t('common.my') + " OTP", [], "/index/myOTP", [], true));
    },
    updatePassword() {
      // 激活“修改密码”
      this.store.applyModule(this.$router, new ModuleItem("update-myself-password", this.$t("updatePassword"), [], "/index/updatePassword", [], true));
    },
    // 打开“切换主题”面板
    showSwitchTheme() {
      this.visibleSwitchTheme = true;
    },
    // 登出
    logout() {
      this.$session.delete('/api/logout').then(() => {
        this.$router.push('/login');
      });
    },
    // 加载MFA功能状态
    loadMFAState(tryDisplayOTP : boolean = false) {
      this.$session.get('/api/mfa/state').then(res => {
        this.showOpenMFA = res.data.supportMFA && !res.data.myselfMFA;
        this.mfaOTPEnabled = res.data.otp;
        if (this.mfaOTPEnabled && tryDisplayOTP) {
          this.displayMyOTP();
        }
      }).catch(() => {
      });
    }
  },
  created() {
    this.loadMFAState();
  }
}
</script>

<template>
  <div class="v-lu-header-icon v-lu-nav-item" @click="this.$refs.userDropdown.handleOpen()">
    <el-dropdown trigger="contextmenu" ref="userDropdown">
      <el-avatar src="src/assets/default-avatar.svg" class="v-lu-header-avatar"/>
      <template #dropdown>
        <el-dropdown-menu>
          <!--            <el-dropdown-item>{{ $t("userCenter") }}</el-dropdown-item>-->
          <el-dropdown-item v-if="showOpenMFA" @click="openMFA">开启MFA</el-dropdown-item>
          <el-dropdown-item v-if="mfaOTPEnabled" @click="displayMyOTP">{{ $t('common.my') }} OTP</el-dropdown-item>
          <el-dropdown-item @click="updatePassword">{{ $t("updatePassword") }}</el-dropdown-item>
          <el-dropdown-item @click="showSwitchTheme">{{ $t("theme.switchTheme") }}</el-dropdown-item>
          <el-dropdown-item @click="logout" divided>{{ $t("logout") }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>

  <div v-if="visibleSwitchTheme">
    <el-drawer v-model="visibleSwitchTheme"
               @close-auto-focus="visibleSwitchTheme=false"
               :show-close="false"
               :title="$t('theme.switchTheme')"
               size="320px"
               class="v-lu-theme-drawer">
      <div class="v-lu-theme-item-container">
        <div class="v-lu-theme-item" v-for="(theme, index) in themes()" :index="index"
             @click="themeStore.applyTheme(index)">
          <el-container class="v-lu-theme-item-layout">
            <el-aside class="v-lu-theme-item-aside" :style="{background: theme.asideMenuBackgroundColor}">
              <div class="v-lu-theme-item-logo-container" :style="{background: theme.asideLogoBackgroundColor}"></div>
            </el-aside>
            <el-container>
              <el-header class="v-lu-theme-item-header" :style="{background: theme.headerBackgroundColor}"></el-header>
              <el-main class="v-lu-theme-item-main"></el-main>
            </el-container>
          </el-container>
          <div class="v-lu-theme-item-name">{{ theme.themeName }}</div>
        </div>
      </div>
    </el-drawer>
  </div>

</template>

<style lang="scss" scoped>
@import "@/assets/css/base.scss";

.v-lu-header-avatar {
  background: #ffffff;
}

.v-lu-theme-item-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-content: flex-start;
  gap: 30px 25px;
  height: 100%;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */

  .v-lu-theme-item {
    width: 125px;
    cursor: pointer;

    .v-lu-theme-item-layout {
      box-shadow: 0 1px 3px var(--el-text-color-secondary);
      border: 1px solid #ffffff;

      .v-lu-theme-item-aside {
        width: 25%;
      }

      .v-lu-theme-item-logo-container {
        height: 20px;
      }

      .v-lu-theme-item-header {
        height: 20px;
        box-shadow: 0 0px 1px var(--el-text-color-secondary);
      }
    }

    .v-lu-theme-item-name {
      text-align: center;
      font-size: 12px;
      font-weight: 500;
      height: 24px;
      line-height: 24px;
      color: #001529;
    }

    .v-lu-theme-item-layout:hover {
      border: 1px solid red;
    }
  }
}

.v-lu-theme-item-container::-webkit-scrollbar {
  width: 0; /* 隐藏滚动条 */
  height: 0;
}

</style>
