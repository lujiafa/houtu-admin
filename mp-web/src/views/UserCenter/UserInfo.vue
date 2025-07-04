<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "UserInfo.vue",
  data() {
    return {
      formParams: {},
    }
  },
  computed: {
    formValidRules() {
      return {
        nickName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 2,
            max: 32,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 2, max: 32}),
            trigger: 'blur'
          }
        ],
        commonRequired: [{required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'}],
      }
    }
  },
  methods: {
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        ownerThis.$session.put("/api/user/updateUserInfo", {
          nickName: this.formParams.nickName,
          phone: this.formParams.phone,
          email: this.formParams.email,
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$t('common.updateSuccessDesc')
          })
        });
      });
    },
    formReset() {
      this.loadUserInfo();
    },
    loadUserInfo() {
      this.$session.get("/api/user/info").then(res => {
        this.formParams = res.data;
      });
    }
  },
  created() {
    this.loadUserInfo();
  },
})
</script>

<template>
  <div class="v-lu-update-password">
    <el-form ref="formRef"
             :model="formParams"
             :label-position="'left'">
      <el-form-item :label="$t('userManage.nickName')" prop="nickName" :rules="formValidRules.nickName">
        <el-input type="nickName" v-model="formParams.nickName"/>
      </el-form-item>
      <el-form-item :label="$t('userManage.phone')" prop="nickName">
        <el-input type="phone" v-model="formParams.phone"/>
      </el-form-item>
      <el-form-item :label="$t('userManage.email')" prop="nickName">
        <el-input type="email" v-model="formParams.email"/>
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
