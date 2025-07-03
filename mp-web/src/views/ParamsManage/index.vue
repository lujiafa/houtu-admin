<script>
import {useStore} from "@/store/index";

export default {
  name: "ParamsManage",
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
        paramName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 1,
            max: 64,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 2, max: 32}),
            trigger: 'blur'
          }
        ],
        paramCode: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            pattern: /^[-_a-zA-Z0-9]{1,64}$/,
            message: this.$i18n.t('commonFormValidRules.lengthCodeRange', {format: '"\.a-zA-Z0-9_-"', min: 1, max: 64}),
            trigger: 'blur'
          }
        ],
        commonRequired: [{required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'}],
      }
    },
    permsAdd() {
      return this.store.verifyActiveModulePerms('system:params:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:params:update');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:params:delete');
    },
  },
  methods: {
    handleAdd() {
      this.formParams = {
        dataType: 'string'
      };
      this.formTitle = this.$i18n.t("common.add");
      this.formVisible = true;
    },
    handleEdit(row) {
      if ((!row || row instanceof Event) && this.selectRows.length === 1) {
        row = this.selectRows[0];
      }
      if (!row) return;
      this.formParams = {
        paramId: row.paramId,
        paramName: row.paramName,
        dataType: row.dataType,
        paramValue: row.paramValue,
        paramDesc: row.paramDesc,
      };
      this.formTitle = this.$i18n.t("common.edit");
      this.formVisible = true;
    },
    handleDelete(row) {
      let delIds = [];
      if (!row || row instanceof Event) {
        let selectRows = this.selectRows;
        if (selectRows.length >= 1) {
          selectRows.forEach((item) => {
            delIds.push(item.paramId);
          });
        }
      } else if (row.paramId) {
        delIds.push(row.paramId);
      }
      if (delIds.length === 0) return;
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/params/delete", {
          data: {
            paramIds: delIds
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
      this.$session.get("/api/sys/params/query", {
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
    // Form表单保存
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.formParams;
        if (!formParams.paramId) {
          ownerThis.$session.post("/api/sys/params/add", {
            paramCode: formParams.paramCode,
            paramName: formParams.paramName,
            dataType: formParams.dataType,
            paramValue: formParams.paramValue,
            paramDesc: formParams.paramDesc
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.addSuccessDesc')
            });
            this.query();
          }).catch(() => {
          });
          ;
        } else {
          ownerThis.$session.put("/api/sys/params/update", {
            paramId: formParams.paramId,
            paramName: formParams.paramName,
            dataType: formParams.dataType,
            paramValue: formParams.paramValue,
            paramDesc: formParams.paramDesc,
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
        <el-form-item :label="$t('paramsManage.paramCode')" prop="paramCode">
          <el-input v-model="queryParams.paramCode"/>
        </el-form-item>
        <el-form-item :label="$t('paramsManage.paramName')" prop="paramName">
          <el-input v-model="queryParams.paramName"/>
        </el-form-item>
        <el-form-item :label="$t('paramsManage.dataType')" prop="dataType">
          <el-select
              v-model.number="queryParams.dataType"
              class="v-lu-el-select"
              clearable
          >
            <el-option :label="$t('paramsManage.dataType_string')" value="string"/>
            <el-option :label="$t('paramsManage.dataType_int')" value="int"/>
            <el-option :label="$t('paramsManage.dataType_boolean')" value="boolean"/>
            <el-option :label="$t('paramsManage.dataType_json')" value="json"/>
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
      <el-button v-if="permsDelete" icon="Delete" @click="handleDelete" :disabled="topDeleteDisabled">
        {{ $t('common.delete') }}
      </el-button>
    </div>
    <el-table
        :data="tableData.records"
        row-key="paramId"
        ref="table-ref"
        v-loading="loading"
        @selection-change="handleSelectRowsEvent"
        border
    >
      <el-table-column type="selection" width="36"/>
      <el-table-column prop="paramId" :label="$t('paramsManage.paramId')"/>
      <el-table-column prop="paramCode" :label="$t('paramsManage.paramCode')"/>
      <el-table-column prop="paramName" :label="$t('paramsManage.paramName')"/>
      <el-table-column prop="paramValue" :label="$t('paramsManage.paramValue')"/>
      <el-table-column prop="dataType" :label="$t('paramsManage.dataType')">
        <template #default="{ row }">
          {{ $t('paramsManage.dataType_' + row.dataType) }}
        </template>
      </el-table-column>
      <el-table-column prop="paramDesc" :label="$t('paramsManage.paramDesc')"/>
      <el-table-column prop="createTime" :label="$t('common.createTime')"/>
      <el-table-column :label="$t('common.operation')">
        <template #default="{ row }">
          <el-button v-if="permsUpdate" type='primary' link @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
          <el-button v-if="permsDelete" type='primary' link @click="handleDelete(row)">{{ $t('common.delete') }}
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
  </div>

  <!-- 添加/编辑弹窗 -->
  <el-dialog v-if="formVisible" v-model="formVisible" :title="formTitle" width="500" draggable>
    <el-form ref="formRef"
             :model="formParams"
             :label-position="'left'">
      <el-form-item :label="$t('paramsManage.paramName')" prop="paramName" :rules="formValidRules.paramName">
        <el-input v-model="formParams.paramName"/>
      </el-form-item>
      <el-form-item v-if="!formParams.paramId" :label="$t('paramsManage.paramCode')" prop="paramCode" :rules="formValidRules.paramCode">
        <el-input v-model="formParams.paramCode"/>
      </el-form-item>
      <el-form-item :label="$t('paramsManage.dataType')" prop="dataType" :rules="formValidRules.commonRequired">
        <el-select
            v-model="formParams.dataType"
            class="v-lu-el-select"
            @change="formParams.paramValue = undefined"
        >
          <el-option :label="$t('paramsManage.dataType_string')" value="string"/>
          <el-option :label="$t('paramsManage.dataType_int')" value="int"/>
          <el-option :label="$t('paramsManage.dataType_boolean')" value="boolean"/>
          <el-option :label="$t('paramsManage.dataType_json')" value="json"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('paramsManage.paramValue')" prop="paramValue" :rules="formValidRules.commonRequired">
        <el-input v-if="formParams.dataType === 'string' || formParams.dataType === 'json'" v-model="formParams.paramValue"/>
        <el-select
            v-else-if="formParams.dataType === 'boolean'"
            v-model="formParams.paramValue"
            class="v-lu-el-select"
        >
          <el-option label="true" value="true"/>
          <el-option label="false" value="false"/>
        </el-select>
        <el-input-number v-else-if="formParams.dataType === 'int'" v-model="formParams.paramValue" />
      </el-form-item>
      <el-form-item :label="$t('paramsManage.paramDesc')" prop="paramDesc">
        <el-input
            v-model="formParams.paramDesc"
            maxlength="512"
            style="width: 320px"
            show-word-limit
            type="textarea"
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
</style>
