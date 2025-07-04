<script lang="ts">
import {defineComponent} from 'vue'

export default defineComponent({
  name: "UserInfo.vue",
  data() {
    return {
      userInfo: {},
      formParams: {},
    }
  },
  computed: {
    // 获取用户手机号
    showUserPhone() {
      if (!this.userInfo || !this.userInfo.phone)
        return "-";
      return this.userInfo.phone;
    },
    // 获取用户邮箱
    showUserEmail() {
      if (!this.userInfo || !this.userInfo.email)
        return "-";
      return this.userInfo.email;
    },
    // 获取用户角色名称
    showUserRoleNames() {
      if (!this.userInfo || !this.userInfo.roleNames || this.userInfo.roleNames.length == 0)
        return "-";
      return this.userInfo.roleNames.join(";");
    },
    // 获取用户组织名称
    showUserOrgNames() {
      if (!this.userInfo || !this.userInfo.orgNames || this.userInfo.orgNames.length == 0)
        return "-";
      return this.userInfo.orgNames.join(";");
    },
    // 获取用户岗位名称
    showUserPostNames() {
      if (!this.userInfo || !this.userInfo.postNames || this.userInfo.postNames.length == 0)
        return "-";
      return this.userInfo.postNames.join(";");
    },
    // 表单验证规则
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
          ownerThis.loadUserInfo();
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
        this.userInfo = res.data;
        this.formParams = {...res.data};
      });
    }
  },
  created() {
    this.loadUserInfo();
  },
})
</script>

<template>
  <div class="v-lu-user-info v-lu-show-user-info">
    <div class="v-lu-user-info-container">
      <el-form-item :label="$t('userManage.userName')">
        {{ userInfo.userName }}
      </el-form-item>
      <el-form-item :label="$t('userManage.nickName')">
        {{ userInfo.nickName }}
      </el-form-item>
      <el-form-item :label="$t('userManage.phone')">
        {{ showUserPhone }}
      </el-form-item>
      <el-form-item :label="$t('userManage.email')">
        {{ showUserEmail }}
      </el-form-item>
      <el-form-item :label="$t('userCenter.roleName')">
        {{ showUserRoleNames }}
      </el-form-item>
      <el-form-item :label="$t('userCenter.orgName')">
        {{ showUserOrgNames }}
      </el-form-item>
      <el-form-item :label="$t('userCenter.postName')">
        {{ showUserPostNames }}
      </el-form-item>
      <el-form-item :label="$t('common.createTime')">
        {{ userInfo.createTime }}
      </el-form-item>
    </div>
  </div>
  <div class="v-lu-user-info v-lu-update-user-info">
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
      <div class="v-lu-update-user-info-btn-container">
        <el-button type="primary" class="v-lu-update-user-info-btn" @click="formReset">{{
            $t('common.reset')
          }}
        </el-button>
        <el-button type="primary" class="v-lu-update-user-info-btn" @click="formSave">{{
            $t('common.confirm')
          }}
        </el-button>
      </div>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.v-lu-user-info {
  float: left;
  min-width: 320px;
}

.v-lu-show-user-info {
  background: #eee;

  .v-lu-user-info-container {
    padding-top: 50px;
    padding-left: 30px;
  }
}

@media screen and (max-width: 720px) {
  .v-lu-show-user-info {
    display: none !important;
  }
}

.v-lu-update-user-info {
  padding: 0px 15px;
  margin: auto;
}

.v-lu-update-user-info-btn-container {
  display: flex;
  justify-content: space-between;

  .v-lu-update-user-info-btn {
    width: 40%;
  }
}
</style>
