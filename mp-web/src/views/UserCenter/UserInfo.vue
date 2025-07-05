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
  <div class="v-lu-userinfo-center v-lu-fill">
    <div class="v-lu-show-userinfo">
      <div class="v-lu-show-userinfo-panel">
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
    <div class="v-lu-update-userinfo">
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
        <div class="v-lu-update-userinfo-btn-container">
          <el-button type="primary" class="v-lu-update-userinfo-btn" @click="formReset">{{
              $t('common.reset')
            }}
          </el-button>
          <el-button type="primary" class="v-lu-update-userinfo-btn" @click="formSave">{{
              $t('common.confirm')
            }}
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped lang="scss">
.v-lu-userinfo-center {
  display: block;

  > div {
    float: left;
    min-height: 520px;
  }
}


.v-lu-show-userinfo {
  background: #eee;
  width: 360px;
  height: 100%;
  margin: 0px;
  box-shadow: 0px 0px 8px 0px black;

  .v-lu-show-userinfo-panel {
    width: 320px;
    padding: 0px 10px 0px 30px;
    margin: 30px auto 0px;
  }
}

.v-lu-update-userinfo {
  width: calc(100% - 390px);
  height: 100%;
  min-width: 300px;
  display: flex;

  > form {
    margin: auto;
  }
}

.v-lu-update-userinfo-btn-container {
  display: flex;
  justify-content: space-between;

  .v-lu-update-userinfo-btn {
    width: 40%;
  }
}

@media screen and (max-width: 839px) {
  .v-lu-show-userinfo {
    width: 320px;

    .v-lu-show-userinfo-panel {
      width: 280px;
    }
  }
}

@media screen and (max-width: 799px) {
  .v-lu-show-userinfo {
    width: 300px;

    .v-lu-show-userinfo-panel {
      width: 260px;
    }
  }
}

@media screen and (max-width: 779px) {
  .v-lu-show-userinfo {
    width: 260px;

    .v-lu-show-userinfo-panel {
      width: 240px;
      padding: 0px 5px 0px 15px;
    }
  }
}

@media screen and (max-width: 739px) {
  .v-lu-show-userinfo {
    width: 100%;
    min-width: 280px;

    .v-lu-show-userinfo-panel {
      width: calc(100% - 40px);
      max-width: 320px;
      padding: 0px 10px 0px 30px;
    }
  }
  .v-lu-update-userinfo {
    width: 100%;
    min-width: 280px;
  }
}
</style>
