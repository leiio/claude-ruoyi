<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    chartData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        title: {
          text: '各分类商品库存占比',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} 件 (占比 {d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: this.chartData.map(item => item.categoryName)
        },
        series: [
          {
            name: '库存数量',
            type: 'pie',
            roseType: 'radius',
            radius: [20, 80],
            center: ['50%', '45%'],
            data: this.chartData.map(item => ({
              name: item.categoryName,
              value: item.totalStock
            })),
            animationEasing: 'cubicInOut',
            animationDuration: 2000,
            label: {
              formatter: '{b}: {c}件'
            }
          }
        ]
      })
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler() {
        this.initChart()
      }
    }
  }
}
</script>
