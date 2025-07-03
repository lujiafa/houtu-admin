<script>
import {useStore} from "@/store/index";

export default {
  name: "OperateLog",
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
        "pageSize": 10,
      },
    }
  },
  methods: {
    // 查询/搜索
    query() {
      if (this.loading) return;
      this.loading = true;
      let queryParams = {
        currentPage: this.queryParams.currentPage,
        pageSize: this.queryParams.pageSize,
      };
      let queryCreateArray = this.queryParams.createTime;
      if (queryCreateArray && queryCreateArray.length > 1) {
        queryParams.beginCreateTime = queryCreateArray[0];
        queryParams.endCreateTime = queryCreateArray[1];
      }
      this.$session.get("/api/sys/logOperate/query", {
        params: queryParams
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
        <el-form-item :label="$t('common.createTime')" prop="createTime">
          <el-date-picker
              v-model="queryParams.createTime"
              type="datetimerange"
              range-separator="To"
              value-format="YYYY-MM-DD HH:mm:ss"
              :start-placeholder="$t('common.beginTime')"
              :end-placeholder="$t('common.endTime')"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="query">{{ $t('common.search') }}</el-button>
          <el-button type="" icon="Refresh" @click="reset">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
        :data="tableData.records"
        row-key="logLoginId"
        ref="table-ref"
        v-loading="loading"
        border
    >
      <el-table-column prop="userId" :label="$t('logOperate.userId')"/>
      <el-table-column prop="ip" label="IP"/>
      <el-table-column prop="userAgent" label="User-Agent"/>
      <el-table-column prop="moduleName" :label="$t('logOperate.moduleName')"/>
      <el-table-column prop="operateType" :label="$t('logOperate.operateType')"/>
      <el-table-column prop="requestParams" :label="$t('logOperate.params')"/>
      <el-table-column prop="responseData" :label="$t('logOperate.responseData')"/>
      <el-table-column prop="costTime" :label="$t('logOperate.costTime')"/>
      <el-table-column prop="createTime" :label="$t('common.createTime')"/>
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
