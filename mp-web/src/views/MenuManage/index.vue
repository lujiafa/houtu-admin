<script>
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import {useStore} from '@/store/index'

export default {
  name: "SystemMenu",
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
      // 控制是否仅能选择子节点，功能按钮级菜单添加和修改时适用
      formTreeSelectOnlyChild: false,
      formMenuTypes: [],
      formParams: {},
    }
  },
  computed: {
    // ElementPlus图标
    formElementPlusIcons() {
      return Object.keys(ElementPlusIconsVue);
    },
    // 添加/修改Form表单校验
    formValidRules() {
      return {
        menuName: [
          {required: true, message: this.$i18n.t('commonFormValidRules.required'), trigger: 'blur'},
          {
            min: 2,
            max: 32,
            message: this.$i18n.t('commonFormValidRules.lengthRange', {min: 2, max: 32}),
            trigger: 'blur'
          }
        ],
        icon: [
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
      return this.store.verifyActiveModulePerms('system:menu:add');
    },
    permsUpdate() {
      return this.store.verifyActiveModulePerms('system:menu:update');
    },
    permsDelete() {
      return this.store.verifyActiveModulePerms('system:menu:delete');
    },
  },
  methods: {
    handleAdd(row) {
      let parentId = 0;
      if (row && !(row instanceof Event)) {
        parentId = row.menuId;
      }
      this.formParams = {
        parentId: parentId,
        status: 1,
        sort: 0,
      };
      this.formTitle = this.$i18n.t("common.add");
      this.resetFormTreeSelect(this.formParams);
      this.treeSelectChange(parentId);
      this.formVisible = true;
    },
    handleEdit(row) {
      if (!row || row instanceof Event) return;
      this.formParams = {...row};
      this.formTitle = this.$i18n.t("common.edit");
      this.resetFormTreeSelect(this.formParams);
      this.treeSelectChange(row.parentId);
      this.formVisible = true;
    },
    handleDelete(row) {
      this.$confirm(this.$i18n.t('common.confirmDeleteDesc'), this.$i18n.t('common.prompt'), {
        confirmButtonText: this.$i18n.t('common.confirm'),
        cancelButtonText: this.$i18n.t('common.cancel'),
        type: 'warning'
      }).then(() => {
        this.$session.delete("/api/sys/menu/delete", {
          params: {
            menuId: row.menuId
          }
        }).then(res => {
          this.$message({
            type: 'success',
            message: this.$i18n.t('common.deleteSuccessDesc')
          });
          this.query();
        }).catch(() => {});
      });
    },
    // 查询/搜索
    query() {
      if (this.loading) return;
      this.loading = true;
      this.$session.get("/api/sys/menu/query", {
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
      this.$refs.queryParamsRef.resetFields();
      this.query()
    },
    /**
     * TreeTable行展开或收起
     */
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
        if (!formParams.menuId) {
          let params = {
            menuName: formParams.menuName,
            menuType: formParams.menuType,
            parentId: formParams.parentId,
            sort: formParams.sort,
            status: formParams.status,
          }
          switch (formParams.menuType) {
            case 1:
              params.iconType = formParams.iconType;
              params.icon = formParams.icon;
              break;
            case 2:
              params.iconType = formParams.iconType;
              params.icon = formParams.icon;
              params.pathType = formParams.pathType;
              params.path = formParams.path;
              params.perms = formParams.perms;
              break;
            case 3:
              params.perms = formParams.perms;
              break;
          }
          ownerThis.$session.post("/api/sys/menu/add", params)
              .then(res => {
                ownerThis.$message({
                  type: 'success',
                  message: this.$i18n.t('common.addSuccessDesc')
                });
                this.query();
              }).catch(() => {});
        } else {
          ownerThis.$session.put("/api/sys/menu/update", {
            menuId: formParams.menuId,
            menuName: formParams.menuName,
            parentId: formParams.parentId,
            iconType: formParams.iconType,
            icon: formParams.icon,
            path: formParams.path,
            sort: formParams.sort,
            status: formParams.status,
            perms: formParams.perms,
          }).then(res => {
            ownerThis.$message({
              type: 'success',
              message: this.$i18n.t('common.updateSuccessDesc')
            });
            this.query();
          }).catch(() => {});
        }
        ownerThis.formVisible = false;
      });
    },
    /**
     * 重置表单TreeSelect数据
     * @param formParams 指定当前编辑Form参数，新增时为空
     * @returns {{label: TranslateResult, value: number}[]}
     */
    resetFormTreeSelect(formParams) {
      let root = {value: 0, label: this.$i18n.t('common.rootNode')}
      if (!formParams.menuId) {
        root.children = this.treeTableData2TreeSelect(this.tableData);
        this.formTreeSelectOnlyChild = false;
      } else if (formParams.menuType === 1) {
        root.children = this.treeTableData2TreeSelect(this.tableData, true, formParams.menuId);
        this.formTreeSelectOnlyChild = false;
      } else if (formParams.menuType === 2) {
        root.children = this.treeTableData2TreeSelect(this.tableData, true, formParams.menuId);
        this.formTreeSelectOnlyChild = false;
      } else if (formParams.menuType === 3) {
        root.children = this.treeTableData2TreeSelect(this.tableData, false);
        this.formTreeSelectOnlyChild = true;
      }
      this.formTreeSelectData = [root];
    },
    /**
     *  Tree表数据转化为TreeSelect格式数据辅助递归方法
     * @param originData 表数据
     * @param onlyDir 过滤输出仅目录
     * @returns {[]}
     */
    treeTableData2TreeSelect(originData, onlyDir = false, excludeId) {
      let data = [];
      originData.forEach(item => {
        if (item.menuType === 3 || item.menuId === excludeId) return;
        if (onlyDir && item.menuType !== 1) return;
        let tmp = {
          value: item.menuId,
          label: item.menuName,
        };
        if (item.children) {
          tmp.children = this.treeTableData2TreeSelect(item.children, onlyDir, excludeId);
        }
        data.push(tmp);
      });
      return data;
    },
    /**
     * 递归从表数据中查找指定菜单
     * @param menuId 查找的菜单ID
     * @param menus 表行数据
     * @returns {*}
     */
    findMenuFormArray(menuId, menus) {
      for (let i = 0; i < menus.length; i++) {
        if (menus[i].menuId === menuId) {
          return menus[i];
        }
        if (menus[i].children) {
          let menu = this.findMenuFormArray(menuId, menus[i].children);
          if (menu) {
            return menu;
          }
        }
      }
      return;
    },
    /**
     *  树形下拉框选择事件
     * @param value 选择值
     */
    treeSelectChange(value) {
      if (!this.formParams.menuId) {
        let parent = this.findMenuFormArray(value, this.tableData);
        if (!parent) {
          this.formParams.menuType = 1;
          this.formMenuTypes = [
            {label: this.$i18n.t('menuManage.menuType_1'), value: 1, disabled: false},
            {label: this.$i18n.t('menuManage.menuType_2'), value: 2, disabled: false},
            {label: this.$i18n.t('menuManage.menuType_3'), value: 3, disabled: true}
          ];
        } else if (parent.menuType === 1) {
          this.formParams.menuType = 2;
          this.formMenuTypes = [
            {label: this.$i18n.t('menuManage.menuType_1'), value: 1, disabled: false},
            {label: this.$i18n.t('menuManage.menuType_2'), value: 2, disabled: false},
            {label: this.$i18n.t('menuManage.menuType_3'), value: 3, disabled: true}
          ];
        } else if (parent.menuType === 2) {
          this.formParams.menuType = 3;
          this.formMenuTypes = [
            {label: this.$i18n.t('menuManage.menuType_1'), value: 1, disabled: true},
            {label: this.$i18n.t('menuManage.menuType_2'), value: 2, disabled: true},
            {label: this.$i18n.t('menuManage.menuType_3'), value: 3, disabled: false}
          ];
        } else {
          this.formMenuTypes = [];
        }
      }
    },
    // 图标类型切换事件
    iconTypeChange(value) {
      this.formParams.icon = '';
      if (value === 2) {
        this.formParams.iconInputReadonly = false;
      } else {
        this.formParams.iconInputReadonly = true;
      }
    },
    // 重定向到Iconify图标库
    redirectIconify() {
      window.open("https://icon-sets.iconify.design/", "_blank");
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
        <el-form-item :label="$t('menuManage.menuName')" prop="menuName">
          <el-input v-model="queryParams.menuName"/>
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
        row-key="menuId"
        ref="table-ref"
        v-loading="loading"
        border
        default-expand-all
    >
      <el-table-column prop="menuName" :label="$t('menuManage.menuName')"/>
      <el-table-column prop="menuType" :label="$t('menuManage.menuType')">
        <template #default="{ row }">{{$t("menuManage.menuType_" + row.menuType)}}</template>
      </el-table-column>
      <el-table-column prop="sort" :label="$t('menuManage.sort')"/>
      <el-table-column prop="perms" :label="$t('menuManage.perms')"/>
      <el-table-column prop="path" :label="$t('menuManage.path')"/>
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
          <el-button type='primary' link v-if="row.menuType !== 3 && row.pathType != 2 && permsAdd" @click="handleAdd(row)">{{
              $t('common.add')
            }}
          </el-button>
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
        <el-form-item class="v-lu-form-label" :label="$t('common.parentNode')"
                      prop="parentId" required>
          <el-tree-select
              v-model="formParams.parentId"
              :data="formTreeSelectData"
              :check-strictly="!formTreeSelectOnlyChild"
              :render-after-expand="false"
              @change="treeSelectChange"
          />
        </el-form-item>
        <el-form-item class="v-lu-form-label" :label="$t('menuManage.menuName')" prop="menuName"
                      :rules="formValidRules.menuName">
          <el-input v-model="formParams.menuName"/>
        </el-form-item>
        <el-form-item v-if="!formParams.menuId" :label="$t('menuManage.menuType')" prop="menuType" :rules="formValidRules.commonRequired">
          <el-radio-group v-model.number="formParams.menuType">
            <el-radio v-for="item in formMenuTypes" :value="item.value" size="large" :disabled="item.disabled">
              {{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="formParams.menuType === 1 || formParams.menuType === 2" :label="$t('menuManage.iconType')"
                      prop="iconType" :rules="formValidRules.commonRequired">
          <el-radio-group v-model.number="formParams.iconType" @change="iconTypeChange">
            <el-radio :value="1" size="large">ElementPlus</el-radio>
            <el-radio :value="2" size="large" @dblclick="redirectIconify" :title="$t('menuManage.iconifyDesc')">Iconify</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="formParams.menuType === 1 || formParams.menuType === 2" :label="$t('menuManage.icon')"
                      prop="icon" :rules="formValidRules.icon">
          <el-popover v-if="formParams.iconType === 1" placement="bottom" trigger="click" :width="380">
            <template #reference>
              <el-input v-model="formParams.icon" readonly/>
            </template>
            <div class="v-lu-icon-container">
              <el-icon v-for="(iconName, index) in formElementPlusIcons" @click="formParams.icon = iconName">
                <component :is="iconName"/>
                <span style="display: none;">{{ iconName }}</span></el-icon>
            </div>
          </el-popover>
          <el-input v-else v-model="formParams.icon"/>
        </el-form-item>
        <el-form-item v-if="!formParams.menuId && formParams.menuType === 2" :label="$t('menuManage.pathType')"
                      prop="pathType" :rules="formValidRules.commonRequired">
          <el-radio-group v-model.number="formParams.pathType">
            <el-radio :value="1" size="large">{{ $t('menuManage.pathType_1') }}</el-radio>
            <el-radio :value="2" size="large">{{ $t('menuManage.pathType_2') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="formParams.menuType === 2" :label="$t('menuManage.path')"
                      prop="path" :rules="formValidRules.commonRequired">
          <el-input v-model="formParams.path"/>
        </el-form-item>
        <el-form-item v-if="formParams.menuType === 2 || formParams.menuType === 3" :label="$t('menuManage.perms')"
                      prop="perms" :rules="formParams.menuType === 3 ? formValidRules.commonRequired : ()=>{}">
          <el-input v-model="formParams.perms"/>
        </el-form-item>
        <el-form-item :label="$t('menuManage.sort')" prop="sort" :rules="formValidRules.commonRequired">
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
<style lang="scss">
.v-lu-icon-container {
  max-height: 200px;
  overflow-y: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
  .el-icon {
    cursor: pointer;
    float: left;
    width: 36px;
    height: 36px;
    font-size: 18px;
    border: 1px solid #ebeef5;
    margin: 3px;
  }
}

.v-lu-icon-container::-webkit-scrollbar {
  width: 0; /* 隐藏滚动条 */
  height: 0;
}
</style>
