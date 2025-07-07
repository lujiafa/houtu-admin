<script>
import {useStore} from "@/store/index";

export default {
  name: "Role",
  data() {
    return {
      store: useStore(),
      loading: false,
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
      formTreeData: [],
      formHasAdmin: false,
      formParams: {},
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
        roleName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 2,
            max: 32,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 2, max: 32}),
            trigger: 'blur'
          }
        ],
        rolePerms: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            pattern: /^[-_a-zA-Z0-9]{2,32}$/,
            message: this.$i18n.t('commonFormValidRules.lengthCodeRange', {format: '"a-zA-Z0-9_-"', min: 2, max: 32}),
            trigger: 'blur'
          }
        ]
      }
    },
    permsAdd() {
      return this.store.verifyActiveModulePerms('system:role:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:role:update');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:role:delete');
    },
  },
  methods: {
    handleAdd() {
      this.formParams = {
        sort: 0,
        status: 1,
        menuIds: []
      };
      this.formTitle = this.$i18n.t("common.add");
      this.formVisible = true;
      this.rolePermsChange(undefined, []);
    },
    handleEdit(row) {
      if ((!row || row instanceof Event) && this.selectRows.length === 1) {
        row = this.selectRows[0];
      }
      if (!row) return;
      this.formParams = {
        roleId: row.roleId,
        roleName: row.roleName,
        rolePerms: row.rolePerms,
        sort: row.sort,
        status: row.status,
        remark: row.remark,
        menuIds: row.menuIds,
      };
      this.formTitle = this.$i18n.t("common.edit");
      this.formVisible = true;
      this.rolePermsChange(row.rolePerms, [...row.menuIds]);
    },
    handleDelete(row) {
      let delIds = [];
      if (!row || row instanceof Event) {
        let selectRows = this.selectRows;
        if (selectRows.length >= 1) {
          selectRows.forEach((item) => {
            delIds.push(item.roleId);
          });
        }
      } else if (row.roleId) {
        delIds.push(row.roleId);
      }
      if (delIds.length === 0) return;
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmBupostonText: this.$i18n.t('common.confirm'),
        cancelBupostonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/role/delete", {
          data: {
            roleIds: delIds
          }
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$i18n.t('common.deleteSuccessDesc')
          });
          this.query();
        }).catch(()=>{});
      });
    },
    // 查询/搜索
    query() {
      if (this.loading) return;
      this.loading = true;
      this.$session.get("/api/sys/role/query", {
        params: this.queryParams
      }).then(res => {
        this.tableData = res.data;
        this.loading = false;
      }).catch(()=>{
        this.loading = false;
      });
    },
    // 重置搜索表单
    reset() {
      this.$refs.searchFormRef.resetFields();
      this.query()
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
    // 角色权限值改变时触发事件
    rolePermsChange(rolePerms, menuIds) {
      // 判断角色权限标识是否指定为超级管理员(admin)
      this.formHasAdmin = /^admin$/i.test(rolePerms);
      // 超级管理员无标注需菜单权限即拥有全部权限，仅对非超级管理员角色时才进行菜单权限设置
      if (!this.formHasAdmin) {
        if (this.formTreeData.length === 0) {
          this.$session.get("/api/sys/role/menuList")
              .then(res => {
                let menuTreeData = res.data || [];
                this.toolParseFormMenuTreeData(menuTreeData, []);
                this.formTreeData = menuTreeData;
                if (!this.formHasAdmin && menuIds && menuIds.length > 0) {
                  this.$nextTick(() => {
                    this.$refs.formTreeRef.setCheckedKeys(menuIds || [], false)
                  });
                }
              }).catch(()=>{});
        } else {
          this.$nextTick(() => {
            this.$refs.formTreeRef.setCheckedKeys(menuIds || [], false)
          });
        }
      }
    },
    // 工具方法：递归解析Form Tree数据，为其添加祖先列表和子孙列表到属性
    toolParseFormMenuTreeData(menuArray, ancestorsIds) {
      let tmpAncestorsIds = ancestorsIds || [];
      let tmpChildrenIds = [];
      menuArray.forEach((item, index) => {
        item.ancestorsIds = tmpAncestorsIds;
        if (item.children && item.children.length > 0) {
          let childrenIds = this.toolParseFormMenuTreeData(item.children, [item.menuId, ...tmpAncestorsIds]) || [];
          if (childrenIds && childrenIds.length > 0) {
            item.childrenIds = childrenIds;
            tmpChildrenIds = [...childrenIds, ...tmpChildrenIds];
          }
        }
        tmpChildrenIds.push(item.menuId);
      });
      return tmpChildrenIds;
    },
    // Form Tree Check事件
    formTreeCheck(data, checkNodes) {
      let checkedKeys = checkNodes.checkedKeys;
      if (checkedKeys.find(item => item === data.menuId)) {
        if (data.ancestorsIds && data.ancestorsIds.length > 0) {
          // 合并和去重
          checkedKeys = [...new Set([...checkedKeys, ...data.ancestorsIds])];
          this.$refs.formTreeRef.setCheckedKeys(checkedKeys, false);
        }
      } else {
        let childrenIds = data.childrenIds;
        if (childrenIds && childrenIds && childrenIds.length > 0) {
          let tmpCheckedKeys = checkedKeys.filter(item => !childrenIds.find(p => p === item));
          this.$refs.formTreeRef.setCheckedKeys(tmpCheckedKeys, false);
        }
      }
    },
    // Form表单保存
    formSave() {
      let menuIds = this.formHasAdmin ? [] : this.$refs.formTreeRef.getCheckedKeys();
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.formParams;
        if (!formParams.roleId) {
          ownerThis.$session.post("/api/sys/role/add", {
            roleName: formParams.roleName,
            rolePerms: formParams.rolePerms,
            sort: formParams.sort,
            status: formParams.status,
            remark: formParams.remark,
            menuIds: menuIds,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.addSuccessDesc')
            });
            this.query();
          }).catch(()=>{});
        } else {
          ownerThis.$session.put("/api/sys/role/update", {
            roleId: formParams.roleId,
            roleName: formParams.roleName,
            rolePerms: formParams.rolePerms,
            sort: formParams.sort,
            status: formParams.status,
            remark: formParams.remark,
            menuIds: menuIds,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.updateSuccessDesc')
            });
            this.query();
          }).catch(()=>{});
        }
        ownerThis.formVisible = false;
      });
    }
  },
  created() {
    this.query();
  }
}
</script>

<template>
  <div class="v-lu-content-container">
    <div class="v-lu-search-container">
      <el-form ref="searchFormRef"
               :inline="true"
               :model="queryParams">
        <el-form-item :label="$t('roleManage.roleName')" prop="roleName">
          <el-input v-model="queryParams.roleName"/>
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
      <el-button v-if="permsUpdate" icon="Edit" @click="handleEdit" :disabled="topEditDisabled">{{ $t('common.edit') }}</el-button>
      <el-button v-if="permsDelete" icon="Delete" @click="handleDelete" :disabled="topDeleteDisabled">{{ $t('common.delete') }}</el-button>
    </div>
    <el-table
        :data="tableData.records"
        row-key="roleId"
        ref="table-ref"
        v-loading="loading"
        @selection-change="handleSelectRowsEvent"
        border
    >
      <el-table-column type="selection" width="36"/>
      <el-table-column prop="roleId" :label="$t('roleManage.roleId')"/>
      <el-table-column prop="roleName" :label="$t('roleManage.roleName')"/>
      <el-table-column prop="rolePerms" :label="$t('roleManage.rolePerms')"/>
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
          <el-button v-if="permsUpdate" type='primary' link @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
          <el-button v-if="permsDelete" type='primary' link @click="handleDelete(row)">{{ $t('common.delete') }}</el-button>
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
  </div>

  <!-- 添加/编辑弹窗 -->
  <el-dialog v-if="formVisible" v-model="formVisible" :title="formTitle" width="500" draggable>
    <el-form ref="formRef"
             :model="formParams"
             :label-position="'left'">
      <el-form-item :label="$t('roleManage.roleName')" prop="roleName" :rules="formValidRules.roleName">
        <el-input v-model="formParams.roleName"/>
      </el-form-item>
      <el-form-item :label="$t('roleManage.rolePerms')" prop="rolePerms" required :rules="formValidRules.rolePerms">
        <el-input v-model="formParams.rolePerms" @change="rolePermsChange(formParams.rolePerms, formParams.menuIds)"/>
      </el-form-item>
      <el-form-item :label="$t('roleManage.sort')" prop="sort" required>
        <el-input-number v-model="formParams.sort" :step="1" :min="0" :max="1000"/>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status" required>
        <el-radio-group v-model.number="formParams.status">
          <el-radio :value="1" size="large">{{ $t('common.enable') }}</el-radio>
          <el-radio :value="0" size="large">{{ $t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('common.remark')" prop="remark">
        <el-input
            v-model="formParams.remark"
            maxlength="256"
            style="width: 320px"
            show-word-limit
            type="textarea"
        />
      </el-form-item>
      <!-- 超管角色不需要设置角色菜单 -->
      <el-form-item v-if="!formHasAdmin" :label="$t('roleManage.roleMenus')" prop="menus">
        <div class="v-lu-form-tree">
          <el-tree
              ref="formTreeRef"
              :data="formTreeData"
              show-checkbox
              default-expand-all
              check-strictly
              node-key="menuId"
              highlight-current
              :props="{label:'menuName'}"
              @check="formTreeCheck"
          />
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="this.formVisible=false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="formSave">{{ $t('common.confirm') }}</el-button>
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

.v-lu-form-tree {
  width: 320px;
  border: 1px solid var(--el-color-info-light-5);
  max-height: 600px;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}
.v-lu-form-tree::-webkit-scrollbar {
  width: 0; /* 隐藏滚动条 */
  height: 0;
}
</style>
