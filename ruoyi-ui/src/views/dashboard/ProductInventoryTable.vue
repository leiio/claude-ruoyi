<template>
  <div class="table-wrapper">
    <div class="table-title">各分类商品库存统计</div>
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="categoryName" label="分类名称" align="center" />
      <el-table-column prop="productCount" label="商品种类数" align="center" width="120" />
      <el-table-column prop="totalStock" label="总库存数量" align="center" width="130">
        <template slot-scope="scope">
          {{ scope.row.totalStock }} 件
        </template>
      </el-table-column>
      <el-table-column prop="totalCost" label="总成本金额" align="center" width="140">
        <template slot-scope="scope">
          ¥{{ Number(scope.row.totalCost || 0).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) }}
        </template>
      </el-table-column>
      <el-table-column label="库存占比" align="center" width="120">
        <template slot-scope="scope">
          {{ getPercent(scope.row.totalStock) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'ProductInventoryTable',
  props: {
    tableData: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    getPercent(stock) {
      if (!this.tableData.length) return '0%'
      const total = this.tableData.reduce((sum, item) => sum + Number(item.totalStock || 0), 0)
      if (total === 0) return '0%'
      return ((stock / total) * 100).toFixed(1) + '%'
    }
  }
}
</script>

<style lang="scss" scoped>
.table-wrapper {
  background: #fff;
  padding: 16px;

  .table-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 16px;
    text-align: center;
  }
}
</style>
