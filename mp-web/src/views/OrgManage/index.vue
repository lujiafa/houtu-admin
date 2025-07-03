<script>
import {useStore} from "@/store/index";
export default {
  name: "Org",
  data() {
    return {
      store: useStore(),
      loading: false,
      tableData: [],
      rowExpand: true,
      queryParams: {},
      // 添加/修改Form弹窗状态
      formVisible: false,
      formTitle: '',
      formTreeSelectData: [],
      formParams: {},
    }
  },
  computed: {
    // 添加/修改Form表单校验
    formValidRules() {
      return {
        orgName: [
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
    },
    permsAdd() {
      return this.store.verifyActiveModulePerms('system:org:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:org:update');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:org:delete');
    },
  },
  methods: {
    handleAdd(row) {
      let parentId = 0;
      if (row && !(row instanceof Event)) {
        parentId = row.orgId;
      }
      this.formParams = {
        parentId: parentId,
        sort: 0,
        status: 1
      };
      this.resetFormTreeSelect();
      this.formTitle = this.$i18n.t("common.add");
      this.formVisible = true;
    },
    handleEdit(row) {
      if (!row || row instanceof Event) return;
      this.formParams = {...row};
      this.resetFormTreeSelect(row.orgId);
      this.formTitle = this.$i18n.t("common.edit");
      this.formVisible = true;
    },
    handleDelete(row) {
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/org/delete", {
          params: {
            orgId: row.orgId
          }
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$i18n.t('common.deleteSuccessDesc')
          });
          this.query();
        })
      }).catch(()=>{});
    },
    // 查询/搜索
    query() {
      if (this.loading) return;
      this.loading = true;
      this.$session.get("/api/sys/org/query", {
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
      this.$refs.queryParamsRef.resetFields();
      this.query()
    },
    // 行展开或收起
    expandOrCollapseToggle() {
      this.rowExpand = !this.rowExpand;
      if (this.tableData) {
        this.tableData.forEach((item) => {
          this.$refs["table-ref"].toggleRowExpansion(item, this.rowExpand);
        })
      }
    },
    // Form表单保存
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.formParams;
        if (!formParams.orgId) {
          ownerThis.$session.post("/api/sys/org/add", {
            orgName: formParams.orgName,
            email: formParams.email,
            phone: formParams.phone,
            parentId: formParams.parentId,
            sort: formParams.sort,
            status: formParams.status
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.addSuccessDesc')
            });
            this.query();
          }).catch(()=>{});
        } else {
          ownerThis.$session.put("/api/sys/org/update", {
            orgId: formParams.orgId,
            orgName: formParams.orgName,
            email: formParams.email,
            phone: formParams.phone,
            parentId: formParams.parentId,
            sort: formParams.sort,
            status: formParams.status
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
    },
    /**
     * 重置表单TreeSelect数据
     * @param formParams 指定当前编辑Form参数，新增时为空
     * @returns {{label: TranslateResult, value: number}[]}
     */
    resetFormTreeSelect(orgId) {
      let root = {value: 0, label: this.$i18n.t('common.rootNode')}
      let tableData = this.tableData;
      if (tableData && tableData.length > 0) {
        if (orgId) {
          root.children = this.transTreeSelectData(tableData, orgId);
        } else {
          root.children = this.transTreeSelectData(tableData);
        }
      }
      this.formTreeSelectData = [root];
    },
    transTreeSelectData(nodeArray, excludeId) {
      let data = [];
      nodeArray.forEach(item => {
        if (excludeId && item.orgId === excludeId) return;
        let tmp = {
          value: item.orgId,
          label: item.orgName,
        };
        if (item.children) {
          tmp.children = this.transTreeSelectData(item.children, excludeId);
        }
        data.push(tmp);
      });
      return data;
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
      <el-form ref="queryParamsRef"
               :inline="true"
               :model="queryParams">
        <el-form-item :label="$t('orgManage.orgName')" prop="orgName">
          <el-input v-model="queryParams.orgName"/>
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
      <el-button icon="Sort" @click="expandOrCollapseToggle">{{ $t('common.expandOrCollapse') }}</el-button>
    </div>
    <el-table
        :data="tableData"
        row-key="orgId"
        ref="table-ref"
        v-loading="loading"
        border
        default-expand-all
    >
      <el-table-column prop="orgName" :label="$t('orgManage.orgName')"/>
      <el-table-column prop="phone" :label="$t('orgManage.phone')"/>
      <el-table-column prop="email" :label="$t('orgManage.email')"/>
      <el-table-column prop="statusText" :label="$t('common.status')">
        <template #default="{ row }">
          <el-tag :type='row["status"] === 1 ? "success" : "warning"'>
            {{ row["status"] === 1 ? $t('common.enable') : $t('common.disable') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('common.createTime')"/>
      <el-table-column prop="updateTime" :label="$t('common.updateTime')"/>
      <el-table-column :label="$t('common.operation')">
        <template #default="{ row }">
          <el-button v-if="permsAdd" type='primary' link @click="handleAdd(row)">{{ $t('common.add') }}</el-button>
          <el-button v-if="permsUpdate" type='primary' link @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
          <el-button v-if="permsDelete" type='primary' link @click="handleDelete(row)">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-if="formVisible" v-model="formVisible" :title="formTitle" width="500" draggable>
      <el-form ref="formRef"
               :model="formParams"
               :label-position="'left'">
        <el-form-item class="v-lu-form-label" :label="$t('common.parentNode')" prop="parentId" required>
          <el-tree-select
              v-model="formParams.parentId"
              :data="formTreeSelectData"
              check-strictly
              :render-after-expand="true"
          />
        </el-form-item>
        <el-form-item class="v-lu-form-label" :label="$t('orgManage.orgName')" prop="orgName"
                      :rules="formValidRules.orgName">
          <el-input v-model="formParams.orgName"/>
        </el-form-item>
        <el-form-item class="v-lu-form-label" :label="$t('orgManage.phone')" prop="phone">
          <el-input v-model="formParams.phone"/>
        </el-form-item>
        <el-form-item class="v-lu-form-label" :label="$t('orgManage.email')" prop="email">
          <el-input v-model="formParams.email"/>
        </el-form-item>
        <el-form-item :label="$t('orgManage.sort')" prop="sort" :rules="formValidRules.commonRequired">
          <el-input-number v-model="formParams.sort" :step="1" :min="0" :max="1000"/>
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status" :rules="formValidRules.commonRequired">
          <el-radio-group v-model.number="formParams.status">
            <el-radio :value="1" size="large">{{ $t('common.enable') }}</el-radio>
            <el-radio :value="0" size="large">{{ $t('common.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="this.formVisible=false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="formSave">{{ $t('common.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
@import "@/assets/css/base.scss";

.v-lu-content-container {
  @include v-lu-fill;
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
}
</style>
