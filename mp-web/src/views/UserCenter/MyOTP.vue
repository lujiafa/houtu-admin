<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "MyMFA.vue",
  data() {
    return {
      mfaQrcodeImg: undefined,
    }
  },
  methods: {
    resetMFA() {
      let that = this;
      this.$confirm(this.$i18n.t('common.resetMFADesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        that.$session.post("/api/mfa/resetOTP").then(res => {
          that.$message({
            type: 'success',
            message: that.$i18n.t('common.operateSuccess')
          });
          that.mfaQrcodeImg = res.data;
        }).catch(() => {
        });
      });
    },
    loadOTPQrcode() {
      this.$session.get("/api/mfa/showOTP").then(res => {
        this.mfaQrcodeImg = res.data;
      }).catch(() => {
      });
    }
  },
  created() {
    this.loadOTPQrcode();
  }
})
</script>

<template>
  <div class="v-lu-mfa-container">
    <div class="v-lu-mfa-qrcode">
      <img :src="mfaQrcodeImg"/>
    </div>
    <div><el-button class="v-lu-mfa-reset" type="primary" @click="resetMFA">{{ $t('common.reset') }}</el-button></div>
  </div>
</template>

<style scoped lang="scss">
.v-lu-mfa-container {
  margin: auto;

  .v-lu-mfa-qrcode {
    width: 200px;
    height: 200px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .v-lu-mfa-reset {
    width: 200px;
  }
}
</style>
