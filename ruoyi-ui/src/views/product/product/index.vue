<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品编码" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入商品编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="商品分类" clearable>
          <el-option
            v-for="dict in categoryOptions"
            :key="dict.id"
            :label="dict.label"
            :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="商品状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="下架" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['product:product:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['product:product:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['product:product:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="商品ID" align="center" prop="productId" width="80" />
      <el-table-column label="商品名称" align="center" prop="productName" min-width="150" show-overflow-tooltip />
      <el-table-column label="商品编码" align="center" prop="productCode" width="120" />
      <el-table-column label="商品分类" align="center" prop="categoryName" width="100" />
      <el-table-column label="价格" align="center" prop="price" width="100">
        <template slot-scope="scope">
          ¥{{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column label="成本价" align="center" prop="costPrice" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.costPrice">¥{{ scope.row.costPrice }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="库存" align="center" prop="stock" width="80" />
      <el-table-column label="单位" align="center" prop="productUnit" width="60" />
      <el-table-column label="促销" align="center" prop="promote" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.promote === '1'" type="danger">促销</el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="促销价" align="center" prop="promotePrice" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.promotePrice">¥{{ scope.row.promotePrice }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['product:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['product:product:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改商品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品编码" prop="productCode">
              <el-input v-model="form.productCode" placeholder="请输入商品编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择商品分类">
                <el-option
                  v-for="dict in categoryOptions"
                  :key="dict.id"
                  :label="dict.label"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品单位" prop="productUnit">
              <el-input v-model="form.productUnit" placeholder="如：件、箱、台" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="商品价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" controls-position="right" placeholder="请输入商品价格" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="成本价" prop="costPrice">
              <el-input-number v-model="form.costPrice" :min="0" :precision="2" controls-position="right" placeholder="请输入成本价" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="库存数量" prop="stock">
              <el-input-number v-model="form.stock" :min="0" controls-position="right" placeholder="请输入库存数量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品重量" prop="productWeight">
              <el-input-number v-model="form.productWeight" :min="0" :precision="2" controls-position="right" placeholder="请输入重量(kg)" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否促销" prop="promote">
              <el-radio-group v-model="form.promote">
                <el-radio label="0">否</el-radio>
                <el-radio label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.promote === '1'">
            <el-form-item label="促销价格" prop="promotePrice">
              <el-input-number v-model="form.promotePrice" :min="0" :precision="2" controls-position="right" placeholder="请输入促销价格" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="商品描述" prop="productDesc">
          <el-input v-model="form.productDesc" type="textarea" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProduct, getProduct, addProduct, updateProduct, delProduct } from '@/api/product/product'
import { listCategory } from '@/api/product/category'

export default {
  name: 'SysProduct',
  data() {
    return {
      loading: true,
      showSearch: true,
      ids: [],
      single: true,
      multiple: true,
      total: 0,
      productList: [],
      categoryOptions: [],
      title: '',
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        productName: undefined,
        productCode: undefined,
        categoryId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        productName: [{ required: true, message: '商品名称不能为空', trigger: 'blur' }],
        productCode: [{ required: true, message: '商品编码不能为空', trigger: 'blur' }],
        categoryId: [{ required: true, message: '商品分类不能为空', trigger: 'change' }],
        price: [{ required: true, message: '商品价格不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getCategoryTreeselect()
  },
  methods: {
    getList() {
      this.loading = true
      listProduct(this.queryParams).then(response => {
        this.productList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getCategoryTreeselect() {
      listCategory().then(response => {
        this.categoryOptions = response.data.map(item => ({
          id: item.categoryId,
          label: item.categoryName
        }))
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        productName: undefined,
        productCode: undefined,
        categoryId: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.productId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加商品'
    },
    handleUpdate(row) {
      this.reset()
      const productId = row.productId || this.ids
      getProduct(productId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改商品'
      })
    },
    handleStatusChange(row) {
      const text = row.status === '0' ? '启用' : '停用'
      this.$modal.confirm('确认要"' + text + '"商品"' + row.productName + '"吗？').then(() => {
        return updateProduct({ productId: row.productId, status: row.status })
      }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === '0' ? '1' : '0'
      })
    },
    handleDelete(row) {
      const productIds = row.productId || this.ids
      this.$modal.confirm('是否确认删除商品"' + row.productName + '"？').then(() => {
        return delProduct(productIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/product/export', {
        ...this.queryParams
      }, `商品数据_${new Date().getTime()}.xlsx`)
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.productId != undefined) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addProduct(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        productId: undefined,
        productName: undefined,
        productCode: undefined,
        categoryId: undefined,
        price: 0,
        costPrice: undefined,
        stock: 0,
        productUnit: undefined,
        productWeight: undefined,
        productDesc: undefined,
        status: '0',
        promote: '0',
        promotePrice: undefined,
        remark: undefined
      }
    }
  }
}
</script>
