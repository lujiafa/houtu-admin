<script>
import {useStore} from '@/store/index.ts'

export default {
  name: "User",
  data() {
    return {
      store: useStore(),
      // 当前用户是否为超管用户
      myselfIsAdmin: false,
      loading: false,
      // 组织机构树数据
      orgTreeData: [],
      // 当前选择的组织结构节点ID
      currentSelectedOrgId: undefined,
      tableData: {
        "currentPage": 1,
        "pageSize": 10,
        "totalPages": 0,
        "totalRecords": 0,
        "records": []
      },
      queryParams: {
        "currentPage": 1,
        "pageSize": 10
      },
      selectRows: [],
      // 添加/修改Form弹窗状态
      formVisible: false,
      formTitle: '',
      formPostData: [],
      formParams: {},
      // 授权Form弹窗信息
      formAuthorizeVisible: false,
      formAuthorizeParams: {},
      formAuthorizeRoelData: [],
      // Secret
      secretDialogVisible: false,
      secretData: {
        password: "",

      },
    }
  },
  computed: {
    // Toolbar编辑功能是否禁用状态
    topEditDisabled() {
      return this.selectRows.length !== 1;
    },
    // Toolbar删除功能是否禁用状态
    topDeleteDisabled() {
      return this.selectRows.length <= 0;
    },
    // 添加/修改Form表单校验
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
        userName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            pattern: /^[-_a-zA-Z0-9]{2,32}$/,
            message: this.$i18n.t('commonFormValidRules.lengthCodeRange', {format: '"a-zA-Z0-9_-"', min: 2, max: 32}),
            trigger: 'blur'
          }
        ],
        commonRequired: [{required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'}],
      }
    },
    permsAdd() {
      return this.store.verifyActiveModulePerms('system:user:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:user:update');
    },
    permsAuth() {
      return this.store.verifyActiveModulePerms('system:user:authorize');
    },
    permsResetSecret() {
      return this.store.verifyActiveModulePerms('system:user:secret:reset');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:user:delete');
    },
  },
  methods: {
    handleAdd() {
      this.formParams = {
        status: 1,
        orgIds: this.currentSelectedOrgId ? [this.currentSelectedOrgId] : [],
      };
      this.loadFormPostData();
      this.formTitle = this.$i18n.t("common.add");
      this.formVisible = true;
    },
    handleAuthorize(row) {
      this.formAuthorizeParams = {
        userId: row.userId,
        userName: row.userName,
        roleIds: row.roleIds,
      }
      this.loadFormAuthorizeRoleData();
      this.formAuthorizeVisible = true;
    },
    handleEdit(row) {
      if ((!row || row instanceof Event) && this.selectRows.length === 1) {
        row = this.selectRows[0];
      }
      if (!row) return;
      this.formParams = {
        userId: row.userId,
        nickName: row.nickName,
        userName: row.userName,
        status: row.status,
        orgIds: [...row.orgIds],
        postIds: [...row.postIds],
      };
      this.loadFormPostData();
      this.formTitle = this.$i18n.t("common.edit");
      this.formVisible = true;
    },
    handleDelete(row) {
      let delIds = [];
      if (!row || row instanceof Event) {
        let selectRows = this.selectRows;
        if (selectRows.length >= 1) {
          selectRows.forEach((item) => {
            delIds.push(item.userId);
          });
        }
      } else if (row.userId) {
        delIds.push(row.userId);
      }
      if (delIds.length === 0) return;
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/user/delete", {
          data: {
            userIds: delIds
          }
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$i18n.t('common.deleteSuccessDesc')
          });
          this.query();
        }).catch(() => {
        });
      });
    },
    // 查询/搜索
    query() {
      if (this.loading) return;
      this.loading = true;
      this.$session.get("/api/sys/user/query", {
        params: this.queryParams
      }).then(res => {
        this.tableData = res.data;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    // 重置搜索表单
    reset() {
      this.$refs.searchFormRef.resetFields();
      this.query();
    },
    // 当分页数据发生变化时会触发该事件处理
    handleChangePage(currentPage, pageSize) {
      this.queryParams.currentPage = currentPage;
      this.queryParams.pageSize = pageSize;
      this.query()
    },
    // 当选择行发生变化时会触发该事件处理
    handleSelectRowsEvent(rows) {
      this.selectRows = rows;
    },
    // 加载组织结构树数据
    loadOrgTreeData() {
      this.$session.get("/api/sys/user/orgList")
          .then(res => {
            let orgTreeData = res.data || [];
            this.toolParseOrgTreeData(orgTreeData);
            this.orgTreeData = orgTreeData;
          }).catch(() => {
      });
    },
    toolParseOrgTreeData(orgArray) {
      let childrenIds = [];
      orgArray.forEach((item) => {
        childrenIds.push(item.orgId);
        if (item.children && item.children.length > 0) {
          let tmpChildrenIds = this.toolParseOrgTreeData(item.children);
          item.childrenIds = tmpChildrenIds;
          childrenIds = [...childrenIds, ...tmpChildrenIds];
        }
      });
      return childrenIds;
    },
    // 组织机构树点击事件
    clickOrgTreeNodeEvent(nodeData) {
      if (this.currentSelectedOrgId === nodeData.orgId) return;
      this.currentSelectedOrgId = nodeData.orgId;
      let queryOrgIds = [nodeData.orgId, ...(nodeData.childrenIds || [])].join(",");
      this.queryParams.orgIds = queryOrgIds;
      this.query();
    },
    // 加载Form表单Select岗位数据
    loadFormPostData() {
      if (!this.formPostData || this.formPostData.length === 0) {
        this.$session.get("/api/sys/user/postList")
            .then(res => {
              this.formPostData = res.data || [];
            }).catch(() => {
        });
      }
    },
    // 加载Form 授权角色数据
    loadFormAuthorizeRoleData() {
      if (!this.formAuthorizeRoelData || this.formAuthorizeRoelData.length === 0) {
        this.$session.get("/api/sys/user/roleList")
            .then(res => {
              this.formAuthorizeRoelData = res.data || [];
            }).catch(() => {
        });
      }
    },
    handleResetSecret(row) {
      this.$session.post("/api/sys/user/resetSecret", {
        userId: row.userId
      }).then(res => {
        this.secretData = {
          password: atob(res.data.password),
          otpImgBase64: res.data.otpImgBase64,
        }
        this.secretDialogVisible = true;
      }).catch(() => {
      });
    },
    // Form表单保存
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.formParams;
        if (!formParams.userId) {
          ownerThis.$session.post("/api/sys/user/add", {
            userName: formParams.userName,
            nickName: formParams.nickName,
            email: formParams.email,
            phone: formParams.phone,
            avatar: formParams.avatar,
            status: formParams.status,
            orgIds: formParams.orgIds,
            postIds: formParams.postIds,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.addSuccessDesc')
            });
            this.query();
            this.secretData = {
              password: atob(res.data.password),
              otpImgBase64: res.data.otpImgBase64,
            }
            this.secretDialogVisible = true;
          }).catch(() => {
          });
        } else {
          ownerThis.$session.put("/api/sys/user/update", {
            userId: formParams.userId,
            nickName: formParams.nickName,
            email: formParams.email,
            phone: formParams.phone,
            avatar: formParams.avatar,
            status: formParams.status,
            orgIds: formParams.orgIds,
            postIds: formParams.postIds,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.updateSuccessDesc')
            });
            this.query();
          }).catch(() => {
          });
        }
        ownerThis.formVisible = false;
      });
    },
    formSaveAuthorize() {
      this.$session.put("/api/sys/user/authorize", {
        userId: this.formAuthorizeParams.userId,
        roleIds: this.formAuthorizeParams.roleIds,
      }).then(res => {
        this.$message({
          type: 'success',
          message: this.$i18n.t('userManage.authorizeSuccess')
        });
        this.query();
        this.formAuthorizeVisible = false;
      }).catch(() => {
      });
    },
    async copyContent(content) {
      try {
        await navigator.clipboard.writeText(content);
        this.$message({
          type: 'success',
          message: this.$t('common.contentCopied')
        });
      } catch (err) {
        this.$message({
          type: 'warning',
          message: this.$t("common.copyFail")
        });
      }
    },
    initLoadMyselfHasAdmin() {
      this.$session.get('/api/sys/user/adm').then(res => {
        this.myselfIsAdmin = res.data;
      }).catch(() => {
      })
    }
  },
  created() {
    this.loadOrgTreeData();
    this.initLoadMyselfHasAdmin();
    this.query();
  }
}
</script>
<template>
  <div class="v-lu-content-container">
    <el-container>
      <el-aside width="200px">
        <el-tree
            ref="mainTreeRef"
            :data="orgTreeData"
            check-strictly
            default-expand-all
            highlight-current
            :props="{label: 'orgName'}"
            @node-click="clickOrgTreeNodeEvent"
        />
      </el-aside>
      <el-main>
        <div class="v-lu-search-container">
          <el-form ref="searchFormRef"
                   :inline="true"
                   :model="queryParams">
            <el-form-item :label="$t('userManage.nickName')" prop="nickName">
              <el-input v-model="queryParams.nickName"/>
            </el-form-item>
            <el-form-item :label="$t('userManage.userName')" prop="userName">
              <el-input v-model="queryParams.userName"/>
            </el-form-item>
            <el-form-item :label="$t('common.status')" prop="status">
              <el-select
                  v-model.number="queryParams.status"
                  class="v-lu-el-select"
                  clearable
              >
                <el-option :label="$t('common.disable')" :value="0"/>
                <el-option :label="$t('common.enable')" :value="1"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="query">{{ $t('common.search') }}</el-button>
              <el-button type="" icon="Refresh" @click="reset">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="v-lu-toolbar-container">
          <el-button v-if="permsAdd" icon="Plus" @click="handleAdd">{{ $t('common.add') }}</el-button>
          <el-button v-if="permsUpdate" icon="Edit" @click="handleEdit" :disabled="topEditDisabled">{{
              $t('common.edit')
            }}
          </el-button>
          <el-button v-if="permsDelete" icon="Delete" @click="handleDelete" :disabled="topDeleteDisabled">{{
              $t('common.delete')
            }}
          </el-button>
        </div>
        <el-table
            :data="tableData.records"
            row-key="userId"
            ref="table-ref"
            v-loading="loading"
            @selection-change="handleSelectRowsEvent"
            border
        >
          <el-table-column type="selection" width="36"/>
          <el-table-column prop="userId" :label="$t('userManage.userId')"/>
          <el-table-column prop="userName" :label="$t('userManage.userName')"/>
          <el-table-column prop="nickName" :label="$t('userManage.nickName')"/>
          <el-table-column prop="email" :label="$t('userManage.email')"/>
          <el-table-column prop="phone" :label="$t('userManage.phone')"/>
          <el-table-column prop="statusText" :label="$t('common.status')">
            <template #default="{ row }">
              <el-tag :type='row["status"] === 1 ? "success" : "warning"'>
                {{ row["status"] === 1 ? $t('common.enable') : $t('common.disable') }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('common.createTime')"/>
          <el-table-column :label="$t('common.operation')">
            <template #default="{ row }">
              <span v-if="!myselfIsAdmin && row.admin">-</span>
              <el-button v-if="myselfIsAdmin || (permsAuth && !row.admin)" type='primary' link
                         @click="handleAuthorize(row)">{{ $t('userManage.authorize') }}
              </el-button>
              <el-button v-if="myselfIsAdmin || (permsResetSecret && !row.admin)" type='primary' link
                         @click="handleResetSecret(row)">{{ $t('userManage.resetSecret') }}
              </el-button>
              <el-button v-if="myselfIsAdmin || (permsUpdate && !row.admin)" type='primary' link
                         @click="handleEdit(row)">{{ $t('common.edit') }}
              </el-button>
              <el-button v-if="myselfIsAdmin || (permsDelete && !row.admin)" type='primary' link
                         @click="handleDelete(row)">{{ $t('common.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="tableData.currentPage"
            v-model:page-size="tableData.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            class="v-lu-el-pagination"
            :total="tableData.totalRecords"
            :page-count="tableData.totalPages"
            @change="handleChangePage"
        />
      </el-main>
    </el-container>
  </div>

  <!-- 添加/编辑弹窗 -->
  <el-dialog v-if="formVisible" v-model="formVisible" :title="formTitle" width="500" draggable>
    <el-form ref="formRef"
             :model="formParams"
             :label-position="'left'">
      <el-form-item v-if="!formParams.userId" :label="$t('userManage.userName')" prop="userName"
                    :rules="formValidRules.userName">
        <el-input v-model="formParams.userName"/>
      </el-form-item>
      <el-form-item :label="$t('userManage.nickName')" prop="nickName" :rules="formValidRules.nickName">
        <el-input v-model="formParams.nickName"/>
      </el-form-item>
      <el-form-item :label="$t('userManage.email')" prop="email">
        <el-input v-model="formParams.email"/>
      </el-form-item>
      <el-form-item :label="$t('userManage.phone')" prop="phone">
        <el-input v-model="formParams.phone"/>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status" :rules="formValidRules.commonRequired">
        <el-radio-group v-model.number="formParams.status">
          <el-radio :value="1" size="large">{{ $t('common.enable') }}</el-radio>
          <el-radio :value="0" size="large">{{ $t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('userManage.postIds')" prop="postIds" :rules="formValidRules.commonRequired">
        <el-select
            v-model="formParams.postIds"
            style="width: 382px"
            clearable
            multiple
        >
          <el-option
              v-for="post in formPostData"
              :key="post.postId"
              :label="post.postName"
              :value="post.postId"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('userManage.orgIds')" prop="orgIds" :rules="formValidRules.commonRequired">
        <el-tree-select
            v-model="formParams.orgIds"
            node-key="orgId"
            :data="orgTreeData"
            multiple
            check-strictly
            :render-after-expand="false"
            style="width: 240px"
            :props="{label: 'orgName'}"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="this.formVisible=false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="formSave">{{ $t('common.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 授权弹窗 -->
  <el-dialog v-if="formAuthorizeVisible" v-model="formAuthorizeVisible" :title="$t('userManage.authorize')" width="500"
             draggable>
    <el-form ref="formRef"
             :model="formAuthorizeParams"
             :label-position="'left'">
      <el-form-item :label="$t('userManage.userName')">
        {{ formAuthorizeParams.userName }}
      </el-form-item>
      <el-form-item :label="$t('userManage.roleIds')" prop="roleIds">
        <el-select
            v-model="formAuthorizeParams.roleIds"
            style="width: 382px"
            clearable
            multiple
        >
          <el-option
              v-for="role in formAuthorizeRoelData"
              :key="role.roleId"
              :label="role.roleName"
              :value="role.roleId"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="this.formAuthorizeVisible=false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="formSaveAuthorize">{{ $t('common.confirm') }}</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 密码或OTP等展示 -->
  <el-dialog v-if="secretDialogVisible" v-model="secretDialogVisible" width="360"
             :close-on-press-escape="false"
             :close-on-click-modal="false"
             :show-close="false"
             draggable>
    <div class="v-lu-secret-container">
      <div v-if="secretData.password" class="v-lu-secret-password">
        <b>{{ $t('userManage.password') }}：</b>
        <el-tooltip :content="$t('common.clickCopy')">
          <span class="v-lu-secret-password-text" @click="copyContent(secretData.password)">{{
              secretData.password
            }}</span>
        </el-tooltip>
      </div>
      <div v-if="secretData.otpImgBase64" class="v-lu-secret-otp-container">
        <div class="v-lu-secret-otp-qrcode-container">
          <img class="v-lu-secret-otp-qrcode" :src="secretData.otpImgBase64">
        </div>
        <div class="v-lu-secret-otp-label">{{ $t('userManage.otpQrcode') }}</div>
      </div>
      <div class="v-lu-secret-tip"><span>{{$t('userManage.secretShowTip')}}</span></div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="this.secretDialogVisible=false">{{ $t('common.close') }}</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss">
@import "@/assets/css/base.scss";

.v-lu-content-container {
  height: auto;
  padding-left: 5px;
  padding-top: 15px;
  width: calc(100% - 5px);

  .v-lu-search-container {
    @include v-lu-fill-width;

    // Select选项框样式
    .v-lu-el-select {
      width: 150px;
    }
  }

  .v-lu-toolbar-container {
    @include v-lu-fill-width;
    margin: 0 0 8px;
  }

  .v-lu-el-pagination {
    margin-top: 8px;
    padding-bottom: 6px;
  }
}

.v-lu-secret-container {
  padding: 10px;

  .v-lu-secret-password {
    text-align: center;
    margin: 10px auto;

    .v-lu-secret-password-text {
      color: var(--el-text-color-secondary);
    }
  }

  .v-lu-secret-otp-container {
    .v-lu-secret-otp-qrcode-container {
      display: flex;
      justify-content: center;
      align-items: center;
      .v-lu-secret-otp-qrcode {
        width: 200px;
        height: 200px;
      }
    }
    .v-lu-secret-otp-label {
      text-align: center;
      position: relative;
      top: -22px;
    }
  }

  .v-lu-secret-tip {
    font-size: 12px;
    text-align: center;
    color: red;
  }
}
</style>
