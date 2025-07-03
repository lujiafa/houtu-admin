<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "LoginMFA",
  data() {
    return {
      mfaTypes: [],
      formParams: {},
      sendButtonText: this.$t('mfa.sendButtonText'),
      sendButtonDisabled: false,
    }
  },
  methods: {
    /**
     * 加载MFA类型数据
     */
    loadMFATypes() {
      this.$session.get("/api/mfa/mfaTypes").then(res => {
        this.mfaTypes = res.data;
        if (this.mfaTypes && this.mfaTypes.length > 0) {
          this.formParams.mfaType = this.mfaTypes[0].mfaType;
          this.formParams.send = this.mfaTypes[0].send;
        }
      }).catch(() => {
      });
    },
    /**
     * MFA类型改变事件
     */
    changeMFAType(value) {
      this.formParams.send = value.send;
    },
    /**
     * 发送MFA验证码，不是所有类型都需要发生验证码
     */
    pushVerifyCode() {
      this.$session.post("/api/mfa/send", {
        mfaType: this.formParams.mfaType
      }).then(res => {
        let remainingTime = 10;
        this.sendButtonDisabled = true;
        let interval = setInterval(() => {
          this.sendButtonText = this.$t('mfa.resendButtonText', {remainingTime: remainingTime--})
          if (remainingTime === 0) {
            this.sendButtonDisabled = false;
            this.sendButtonText = this.$t('mfa.sendButtonText');
            clearInterval(interval);
          }
        }, 1000);
      }).catch(() => {
      });
    },
    /**
     * 验证MFA验证码，进入下一步
     */
    nextSubmit() {
      if (!this.formParams.code || this.formParams.code === '') {
        return;
      }
      this.$session.post("/api/mfa/verify", {
        mfaType: this.formParams.mfaType,
        code: this.formParams.code
      }).then(res => {
        this.$session.state = true;
        this.$router.push("/index");
      }).catch(() => {
      });

    }
  },
  created() {
    // 初始化MFA类型数据
    this.loadMFATypes();
  }
})
</script>

<template>
  <div class="v-lu-mfa-body">
    <div class="v-lu-mfa-layout">
      <div class="v-lu-mfa-container">
        <h2>{{ $t('mfa.title') }}</h2>
        <el-form ref="formRef"
                 class="v-lu-mfa-form"
                 :model="formParams">
          <el-form-item prop="mfaCode">
            <el-select
                v-model="formParams.mfaType"
                v-for="(item, index) in mfaTypes" :key="item.mfaType"
                @change="changeMFAType(item)"
            >
              <el-option :label="item.mfaTypeName" :value="item.mfaType"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="code">
            <el-input :placeholder="$t('mfa.inputPlaceHolder')" v-model="formParams.code" :style="formParams.send ? 'width: calc(100% - 30%);max-width: calc(100% - 128px)' : ''"/>
            <el-button v-if="formParams.send" style="margin-left:9%;width:20%;min-width: 90px;" @click="pushVerifyCode" :disabled="sendButtonDisabled">{{sendButtonText}}</el-button>
          </el-form-item>
          <el-button class="v-lu-mfa-btn" type="primary" @click="nextSubmit">{{
              $t('common.nextStep')
            }}
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.v-lu-mfa-body {
  background: var(--el-color-info-light-8);
  width: 100%;
  height: 100%;
}

.v-lu-mfa-layout {
  max-width: 480px;
  margin: 0px auto;
  padding: 100px 20px 20px 20px;

  .v-lu-mfa-container {
    background: #fff;
    padding: 30px 20px;
  }

  .v-lu-mfa-form-item {
    width: 100%;
  }

  .v-lu-mfa-btn {
    width: 100%;
  }
}
</style>
