<script>
import {useStore} from "@/store/index";

export default {
  name: "DistManage",
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

      // 字典项抽屉弹窗
      dictDataDrawerVisible: false,
      dictDataDrawerTitle: '',
      dictDataQueryParams: {},
      dictDataTableData: [],
      dictDataSelectRows: [],
      dictDataFormVisible: false,
      dictDataFormTitle: '',
      dictDataFormParams: {},
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
    // Toolbar编辑功能是否禁用状态
    dictDataTopEditDisabled() {
      return this.dictDataSelectRows.length !== 1;
    },
    // Toolbar删除功能是否禁用状态
    dictDataTopDeleteDisabled() {
      return this.dictDataSelectRows.length <= 0;
    },
    // 添加/修改Form表单校验
    formValidRules() {
      return {
        typeName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 1,
            max: 64,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 1, max: 64}),
            trigger: 'blur'
          }
        ],
        typeCode: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            pattern: /^[-_a-zA-Z0-9]{1,64}$/,
            message: this.$i18n.t('commonFormValidRules.lengthCodeRange', {format: '"\.a-zA-Z0-9_-"', min: 1, max: 64}),
            trigger: 'blur'
          }
        ],
        commonRequired: [{required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'}],
        dictDataName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 1,
            max: 64,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 1, max: 64}),
            trigger: 'blur'
          }
        ],
        dictDataValue: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 1,
            max: 64,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 1, max: 64}),
            trigger: 'blur'
          }
        ],
      }
    },
    permsAdd() {
      return this.store.verifyActiveModulePerms('system:dict:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:dict:update');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:dict:delete');
    },
  },
  methods: {
    handleAdd() {
      this.formParams = {
        status: 1
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
        dictId: row.dictId,
        typeName: row.typeName,
        typeCode: row.typeCode,
        typeDesc: row.typeDesc,
        status: row.status,
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
            delIds.push(item.dictId);
          });
        }
      } else if (row.dictId) {
        delIds.push(row.dictId);
      }
      if (delIds.length === 0) return;
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/dict/delete", {
          data: {
            dictIds: delIds
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
      this.$session.get("/api/sys/dict/query", {
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
    // 行单击
    showDictData(row, col, event) {
      if (event) event.preventDefault();
      this.dictDataTableData = [];
      this.dictDataQueryParams = {
        dictId: row.dictId,
      }
      this.dictDataQuery();
      this.dictDataDrawerTitle = row.typeName;
      this.dictDataDrawerVisible = true;
    },
    // Form表单保存
    formSave() {
      let ownerThis = this;
      this.$refs.formRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.formParams;
        if (!formParams.dictId) {
          ownerThis.$session.post("/api/sys/dict/add", {
            typeName: formParams.typeName,
            typeCode: formParams.typeCode,
            typeDesc: formParams.typeDesc,
            status: formParams.status,
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
          ownerThis.$session.put("/api/sys/dict/update", {
            dictId: formParams.dictId,
            typeName: formParams.typeName,
            typeCode: formParams.typeCode,
            typeDesc: formParams.typeDesc,
            status: formParams.status,
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

    dictDataHandleAdd() {
      this.dictDataFormParams = {
        dictId: this.dictDataQueryParams.dictId,
        status: 1,
        sort: 0,
      };
      this.dictDataFormTitle = this.$i18n.t("common.add");
      this.dictDataFormVisible = true;
    },
    dictDataHandleEdit(row) {
      if ((!row || row instanceof Event) && this.dictDataSelectRows.length === 1) {
        row = this.dictDataSelectRows[0];
      }
      if (!row) return;
      this.dictDataFormParams = {
        dictDataId: row.dictDataId,
        dictDataName: row.dictDataName,
        dictDataValue: row.dictDataValue,
        dictDataDesc: row.dictDataDesc,
        sort: row.sort,
        status: row.status,
      };
      this.dictDataFormTitle = this.$i18n.t("common.edit");
      this.dictDataFormVisible = true;
    },
    dictDataHandleDelete(row) {
      let delIds = [];
      if (!row || row instanceof Event) {
        let selectRows = this.dictDataSelectRows;
        if (selectRows.length >= 1) {
          selectRows.forEach((item) => {
            delIds.push(item.dictDataId);
          });
        }
      } else if (row.dictDataId) {
        delIds.push(row.dictDataId);
      }
      if (delIds.length === 0) return;
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/dictData/delete", {
          data: {
            dictDataIds: delIds
          }
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$i18n.t('common.deleteSuccessDesc')
          });
          this.dictDataQuery();
        }).catch(() => {
        });
      });
    },
    // 查询/搜索 字典项
    dictDataQuery() {
      if (this.loading) return;
      this.loading = true;
      this.$session.get("/api/sys/dictData/query", {
        params: this.dictDataQueryParams
      }).then(res => {
        this.dictDataTableData = res.data;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    // 当选择行发生变化时会触发该事件处理
    dictDataHandleSelectRowsEvent(rows) {
      this.dictDataSelectRows = rows;
    },
    // Form表单保存
    dictDataFormSave() {
      let ownerThis = this;
      this.$refs.dictDataFormRef.validate((valid, err) => {
        if (!valid) return;
        let formParams = ownerThis.dictDataFormParams;
        if (!formParams.dictDataId) {
          ownerThis.$session.post("/api/sys/dictData/add", {
            dictId: formParams.dictId,
            dictDataName: formParams.dictDataName,
            dictDataValue: formParams.dictDataValue,
            dictDataDesc: formParams.dictDataDesc,
            sort: formParams.sort,
            status: formParams.status,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.addSuccessDesc')
            });
            this.dictDataQuery();
          }).catch(() => {
          });
          ;
        } else {
          ownerThis.$session.put("/api/sys/dictData/update", {
            dictDataId: formParams.dictDataId,
            dictDataName: formParams.dictDataName,
            dictDataValue: formParams.dictDataValue,
            dictDataDesc: formParams.dictDataDesc,
            sort: formParams.sort,
            status: formParams.status,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.updateSuccessDesc')
            });
            this.dictDataQuery();
          }).catch(() => {
          });
        }
        ownerThis.dictDataFormVisible = false;
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
        <el-form-item :label="$t('dictManage.typeCode')" prop="typeCode">
          <el-input v-model="queryParams.typeCode"/>
        </el-form-item>
        <el-form-item :label="$t('dictManage.typeName')" prop="typeName">
          <el-input v-model="queryParams.typeName"/>
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
      <el-button v-if="permsDelete" icon="Delete" @click="handleDelete" :disabled="topDeleteDisabled">
        {{ $t('common.delete') }}
      </el-button>
    </div>
    <el-table
        :data="tableData.records"
        row-key="dictId"
        ref="table-ref"
        v-loading="loading"
        @selection-change="handleSelectRowsEvent"
        border
        @row-dblclick="showDictData"
        @row-contextmenu="showDictData"
    >
      <el-table-column type="selection" width="36"/>
      <el-table-column prop="typeName" :label="$t('dictManage.typeName')" min-width="120"/>
      <el-table-column prop="typeCode" :label="$t('dictManage.typeCode')" min-width="120"/>
      <el-table-column prop="typeDesc" :label="$t('dictManage.typeDesc')" min-width="150" show-overflow-tooltip/>
      <el-table-column prop="statusText" :label="$t('common.status')" min-width="72">
        <template #default="{ row }">
          <el-tag :type='row["status"] === 1 ? "success" : "warning"'>
            {{ row["status"] === 1 ? $t('common.enable') : $t('common.disable') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('common.createTime')" min-width="165"/>
      <el-table-column :label="$t('common.operation')" min-width="165">
        <template #default="{ row }">
          <el-button type='primary' link @click="showDictData(row)">{{ $t('dictManage.dictItem') }}</el-button>
          <el-button v-if="permsUpdate" type='primary' link @click="handleEdit(row)">{{ $t('common.edit') }}</el-button>
          <el-button v-if="permsDelete" type='primary' link @click="handleDelete(row)">{{
              $t('common.delete')
            }}
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
      <el-form-item :label="$t('dictManage.typeName')" prop="typeName" :rules="formValidRules.typeName">
        <el-input v-model="formParams.typeName"/>
      </el-form-item>
      <el-form-item v-if="!formParams.dictId" :label="$t('dictManage.typeCode')" prop="typeCode" required
                    :rules="formValidRules.typeCode">
        <el-input v-model="formParams.typeCode"/>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status" :rules="formValidRules.commonRequired">
        <el-radio-group v-model.number="formParams.status">
          <el-radio :value="1" size="large">{{ $t('common.enable') }}</el-radio>
          <el-radio :value="0" size="large">{{ $t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('dictManage.typeDesc')" prop="typeDesc">
        <el-input
            v-model="formParams.typeDesc"
            maxlength="128"
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

  <!-- 字典项抽屉式弹窗展示 -->
  <el-drawer v-model="dictDataDrawerVisible"
             :title="dictDataDrawerTitle"
             size="85%">
    <div class="v-lu-toolbar-container">
      <el-button v-if="permsAdd" icon="Plus" @click="dictDataHandleAdd">{{ $t('common.add') }}</el-button>
      <el-button v-if="permsUpdate" icon="Edit" @click="dictDataHandleEdit" :disabled="dictDataTopEditDisabled">
        {{ $t('common.edit') }}
      </el-button>
      <el-button v-if="permsDelete" icon="Delete" @click="dictDataHandleDelete" :disabled="dictDataTopDeleteDisabled">
        {{ $t('common.delete') }}
      </el-button>
    </div>
    <el-table
        :data="dictDataTableData"
        row-key="dictDataId"
        ref="table-ref"
        v-loading="loading"
        @selection-change="dictDataHandleSelectRowsEvent"
        border
    >
      <el-table-column type="selection" width="36"/>
      <el-table-column prop="dictDataName" :label="$t('dictManage.dictDataName')" min-width="120"/>
      <el-table-column prop="dictDataValue" :label="$t('dictManage.dictDataValue')" min-width="120"/>
      <el-table-column prop="dictDataDesc" :label="$t('dictManage.dictDataDesc')" min-width="150" show-overflow-tooltip/>
      <el-table-column prop="sort" :label="$t('dictManage.dictDataSort')" min-width="120"/>
      <el-table-column prop="statusText" :label="$t('common.status')" min-width="72">
        <template #default="{ row }">
          <el-tag :type='row["status"] === 1 ? "success" : "warning"'>
            {{ row["status"] === 1 ? $t('common.enable') : $t('common.disable') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('common.createTime')" min-width="165"/>
      <el-table-column :label="$t('common.operation')" min-width="108">
        <template #default="{ row }">
          <el-button v-if="permsUpdate" type='primary' link @click="dictDataHandleEdit(row)">{{
              $t('common.edit')
            }}
          </el-button>
          <el-button v-if="permsDelete" type='primary' link @click="dictDataHandleDelete(row)">{{
              $t('common.delete')
            }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  字典项添加/修改  -->
    <el-dialog v-if="dictDataFormVisible" v-model="dictDataFormVisible" :title="dictDataFormTitle" width="500"
               draggable>
      <el-form ref="dictDataFormRef"
               :model="dictDataFormParams"
               :label-position="'left'">
        <el-form-item :label="$t('dictManage.dictDataName')" prop="dictDataName" :rules="formValidRules.dictDataName">
          <el-input v-model="dictDataFormParams.dictDataName"/>
        </el-form-item>
        <el-form-item v-if="!dictDataFormParams.dictDataId" :label="$t('dictManage.dictDataValue')" prop="dictDataValue"
                      :rules="formValidRules.dictDataValue">
          <el-input v-model="dictDataFormParams.dictDataValue"/>
        </el-form-item>
        <el-form-item :label="$t('dictManage.dictDataSort')" prop="sort" required>
          <el-input-number v-model="dictDataFormParams.sort" :step="1" :min="0" :max="1000"/>
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status" :rules="formValidRules.commonRequired">
          <el-radio-group v-model.number="dictDataFormParams.status">
            <el-radio :value="1" size="large">{{ $t('common.enable') }}</el-radio>
            <el-radio :value="0" size="large">{{ $t('common.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('dictManage.typeDesc')" prop="typeDesc">
          <el-input
              v-model="dictDataFormParams.dictDataDesc"
              maxlength="128"
              style="width: 320px"
              show-word-limit
              type="textarea"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="this.dictDataFormVisible=false">{{ $t('common.cancel') }}</el-button>
          <el-button type="primary" @click="dictDataFormSave">{{ $t('common.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </el-drawer>
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

  .v-lu-el-pagination {
    margin-top: 8px;
    padding-bottom: 6px;
  }
}

.v-lu-toolbar-container {
  @include v-lu-fill-width;
  margin: 0 0 8px;
}
</style>
