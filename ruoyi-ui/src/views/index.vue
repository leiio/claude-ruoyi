<template>
  <div class="dashboard-container">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon icon-category">
            <svg-icon icon-class="tree" class-name="stat-icon-svg" />
          </div>
          <div class="stat-info">
            <div class="stat-label">总分类数</div>
            <div class="stat-value">{{ stats.categoryCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon icon-product">
            <svg-icon icon-class="shopping" class-name="stat-icon-svg" />
          </div>
          <div class="stat-info">
            <div class="stat-label">商品数量</div>
            <div class="stat-value">{{ stats.productCount }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon icon-stock">
            <svg-icon icon-class="clipboard" class-name="stat-icon-svg" />
          </div>
          <div class="stat-info">
            <div class="stat-label">总库存数量</div>
            <div class="stat-value">{{ stats.totalStock }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-icon icon-money">
            <svg-icon icon-class="money" class-name="stat-icon-svg" />
          </div>
          <div class="stat-info">
            <div class="stat-label">总成本金额</div>
            <div class="stat-value">
              <span class="cost-value">¥{{ formatCostValue(stats.totalCost) }}</span>
              <span v-if="isWanUnit(stats.totalCost)" class="cost-unit">万</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :sm="24" :lg="12">
        <product-inventory-table :table-data="inventoryData" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <product-inventory-chart :chart-data="inventoryData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getInventoryStats } from '@/api/product/product'
import ProductInventoryChart from './dashboard/ProductInventoryChart'
import ProductInventoryTable from './dashboard/ProductInventoryTable'

export default {
  name: 'Dashboard',
  components: {
    ProductInventoryChart,
    ProductInventoryTable
  },
  data() {
    return {
      inventoryData: [],
      stats: {
        categoryCount: 0,
        productCount: 0,
        totalStock: 0,
        totalCost: '0.00'
      }
    }
  },
  created() {
    this.getInventoryStats()
  },
  methods: {
    getInventoryStats() {
      getInventoryStats().then(response => {
        this.inventoryData = response.data || []
        this.computeStats()
      })
    },
    computeStats() {
      const data = this.inventoryData
      this.stats.categoryCount = data.length
      this.stats.productCount = data.reduce((sum, item) => sum + Number(item.productCount || 0), 0)
      this.stats.totalStock = data.reduce((sum, item) => sum + Number(item.totalStock || 0), 0)
      this.stats.totalCost = data.reduce((sum, item) => sum + Number(item.totalCost || 0), 0)
    },
    formatCostValue(value) {
      if (value >= 10000) {
        return (value / 10000).toFixed(2)
      }
      return value.toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    isWanUnit(value) {
      return value >= 10000
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: rgb(240, 242, 245);
  min-height: calc(100vh - 84px);

  .stats-row {
    margin-bottom: 20px;
  }

  .stat-card {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    box-shadow: 4px 4px 20px rgba(0, 0, 0, .05);
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 4px 4px 30px rgba(0, 0, 0, .1);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      .stat-icon-svg {
        font-size: 28px;
        color: #fff;
      }

      &.icon-category {
        background: linear-gradient(135deg, #36a3f7, #007bff);
      }

      &.icon-product {
        background: linear-gradient(135deg, #36cfc9, #00b894);
      }

      &.icon-stock {
        background: linear-gradient(135deg, #ffa94d, #ff6b00);
      }

      &.icon-money {
        background: linear-gradient(135deg, #f06595, #e64980);
      }
    }

    .stat-info {
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;

        .cost-unit {
          font-size: 14px;
          color: #f56c6c;
          margin-left: 2px;
        }
      }
    }
  }

  .content-row {
    .chart-wrapper {
      background: #fff;
      padding: 16px;
      height: 100%;
    }
  }
}
</style>
