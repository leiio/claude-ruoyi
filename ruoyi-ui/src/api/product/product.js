import request from '@/utils/request'

// 查询商品列表
export function listProduct(query) {
  return request({
    url: '/system/product/list',
    method: 'get',
    params: query
  })
}

// 查询商品详情
export function getProduct(productId) {
  return request({
    url: '/system/product/' + productId,
    method: 'get'
  })
}

// 新增商品
export function addProduct(data) {
  return request({
    url: '/system/product',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateProduct(data) {
  return request({
    url: '/system/product',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delProduct(productId) {
  return request({
    url: '/system/product/' + productId,
    method: 'delete'
  })
}

// 导出商品
export function exportProduct(query) {
  return request({
    url: '/system/product/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

// 修改商品状态
export function changeStatus(data) {
  return request({
    url: '/system/product/changeStatus',
    method: 'put',
    data: data
  })
}

// 获取各分类商品库存统计
export function getInventoryStats() {
  return request({
    url: '/system/product/inventoryStats',
    method: 'get'
  })
}
