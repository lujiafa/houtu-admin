<script lang="ts">
import {useBaseStore} from "@/store/base";
import {useThemeStore} from "@/store/theme";
import CryptoJS from 'crypto-js';

export default {
  data() {
    return {
      baseStore: useBaseStore(),
      themeStore: useThemeStore(),
      loginForm: {
        username: '',
        password: '',
        captcha: ''
      },
      captchaUrl: new URL('/api/getCaptcha', import.meta.env.VITE_BASE_API_URL).href,
      logoImg: import.meta.env.VITE_APP_LOGO || 'src/assets/default-logo.svg',
    };
  },
  methods: {
    handleLogin() {
      if (this.loginForm.username === '' || this.loginForm.password === '' || this.loginForm.captcha === '') {
        return;
      }
      this.$axios.post('/api/login', null, {
        params: {
          username: this.loginForm.username,
          password: CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(CryptoJS.enc.Utf8.parse(this.loginForm.password), CryptoJS.enc.Utf8.parse(this.loginForm.password))),
          captcha: this.loginForm.captcha
        }
      }).then(res => {
        if (res.status === 200) {
          if (res.data.code === 0) {
            this.$session.state = true;
            this.$router.push("/index");
          } else if (res.data.code === 13) {
            this.$router.push("/mfa");
          } else {
            this.$message({
              type: 'warning',
              message: res.data.message
            });
            this.resetLoadCaptcha();
          }
        }
      });
    },
    resetLoadCaptcha() {
      const baseUrl = import.meta.env.VITE_BASE_API_URL;
      this.captchaUrl = new URL(`/api/getCaptcha?t=${new Date().getTime()}`, baseUrl).href;
    }
  }
};
</script>

<template>
  <div class="v-lu-body-container" :style="themeStore.loginThemeStyle">
    <div class="v-lu-login-container"
         :class="baseStore.mobileView?'v-lu-login-mobile-container':'v-lu-login-pc-container'">
      <div class="v-lu-login-header-container">
        <img v-if="logoImg" :src="logoImg" @error="logoImg=false"/>
        <h2>{{ baseStore.systemName }}</h2>
      </div>
      <el-form class="v-login-form" :model="loginForm" @submit.prevent="handleLogin">
        <el-form-item :label-width="'0'">
          <el-input v-model="loginForm.username" :placeholder="$t('login.usernamePlaceholder')" size="large"
                    :prefix-icon="'User'"/>
        </el-form-item>
        <el-form-item :label-width="'0'">
          <el-input v-model="loginForm.password" :placeholder="$t('login.passwordPlaceholder')" size="large"
                    type="password" :prefix-icon="'Lock'"/>
        </el-form-item>
        <el-form-item :label-width="'0'" class="v-lu-form-captcha-item">
          <el-input v-model="loginForm.captcha" :placeholder="$t('login.captchaPlaceholder')"
                    class="v-lu-form-captcha-input" size="large"
                    :prefix-icon="'Monitor'"/>
          <img :src="captchaUrl" @click="resetLoadCaptcha"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="v-lu-login-btn" native-type="submit">{{
              $t('login.loginBtnText')
            }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.v-lu-body-container {
  --login-container-background-color: "#ffffff";
  background-size: 100%;
  background-image: url("@/assets/default-login-background.svg");
  background-position: 0px 0px;
  background-color: var(--login-container-background-color);
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

}

.v-lu-login-container {
  height: 380px;
  box-shadow: var(--el-box-shadow-light);
  background: var(--el-bg-color);
  border-radius: 3px;

  .v-lu-login-header-container {
    display: flex;
    align-items: center;
    justify-content: center;

    img {
      width: auto;
      margin: 0px 8px 0px 0px;
      position: relative;
      left: -12px;
    }

    h2 {
      text-align: center;
    }
  }

  .v-login-form {
    margin-top: 30px;
  }

  .v-lu-login-btn {
    width: 100%;
  }

  .v-lu-form-captcha-item {
    display: flex;
    justify-content: center;
    align-items: center;

    img {
      cursor: pointer;
      transition: all .3s;
      margin-left: 8px;
      height: 36px;
    }
  }

}

.v-lu-login-pc-container {
  width: 320px;
  padding: 13px 50px 0;

  img {
    width: 110px;
  }

  .v-lu-form-captcha-input {
    width: 120px;
  }
}


.v-lu-login-mobile-container {
  width: 250px;
  padding: 13px 36px 0;

  img {
    width: 110px;
  }

  .v-lu-form-captcha-input {
    width: 120px;
  }
}
</style>
