<script lang="ts">
import {defineComponent} from 'vue'
import CryptoJS from "crypto-js";

export default defineComponent({
  name: "UpdatePassword.vue",
  data() {
    return {
      formParams: {},
    }
  },
  computed: {
    validPassword() {
      return [
        {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
        {
          pattern: /^[-_a-zA-Z\d\W]{8,32}$/,
          message: this.$i18n.t('commonFormValidRules.lengthCodeRange', {
            format: '"a-zA-Z0-9_-!@#$%..."',
            min: 8,
            max: 32
          }),
          trigger: 'blur'
        }
      ]
    },
    validConfirmPassword() {
      return [
        {
          validator: (rule, value, callback) => {
            if (value !== this.formParams.password) {
              callback(new Error(this.$t('commonFormValidRules.validConfirmPassword')));
            } else {
              callback();
            }
          },
          trigger: 'blur'
        }
      ]
    }
  },
  methods: {
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        ownerThis.$session.put("/api/user/updateMyselfPassword", {
          oldPassword: CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(CryptoJS.enc.Utf8.parse(this.formParams.oldPassword), CryptoJS.enc.Utf8.parse(this.formParams.oldPassword))),
          password: CryptoJS.enc.Hex.stringify(CryptoJS.HmacSHA256(CryptoJS.enc.Utf8.parse(this.formParams.password), CryptoJS.enc.Utf8.parse(this.formParams.password)))
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$t('common.updateSuccessDesc')
          })
        });
      });
    },
    formReset() {
      this.$refs.formRef.resetFields();
    }
  }
})
</script>

<template>
  <div class="v-lu-update-password">
    <el-form ref="formRef"
             :model="formParams"
             :label-position="'left'">
      <el-form-item :label="$t('common.oldPassword')" prop="oldPassword" :rules="validPassword">
        <el-input type="password" v-model="formParams.oldPassword"/>
      </el-form-item>
      <el-form-item :label="$t('common.newPassword')" prop="password" :rules="validPassword">
        <el-input type="password" v-model="formParams.password"/>
      </el-form-item>
      <el-form-item :label="$t('common.confirmPassword')" prop="confirmPassword" :rules="validConfirmPassword">
        <el-input type="password" v-model="formParams.confirmPassword"/>
      </el-form-item>
      <div class="v-lu-update-password-btn-container">
        <el-button type="primary" class="v-lu-update-password-btn" @click="formReset">{{ $t('common.reset') }}</el-button>
        <el-button type="primary" class="v-lu-update-password-btn" @click="formSave">{{ $t('common.confirm') }}</el-button>
      </div>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.v-lu-update-password {
  margin: auto;
}

.v-lu-update-password-btn-container {
  display: flex;
  justify-content: space-between;

  .v-lu-update-password-btn {
    width: 40%;
  }
}
</style>
