<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="激活码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入激活码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable
                   @change="handleQuery">
          <el-option
            v-for="dict in dict.type.sys_activation_code_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="绑定用户ID" prop="userId">-->
<!--        <el-input-->
<!--          v-model="queryParams.userId"-->
<!--          placeholder="请输入绑定用户ID"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="Telegram用户ID" prop="telegramId">-->
<!--        <el-input-->
<!--          v-model="queryParams.telegramId"-->
<!--          placeholder="请输入Telegram用户ID"-->
<!--          clearable-->
<!--          @keyup.enter.native="handleQuery"-->
<!--        />-->
<!--      </el-form-item>-->
<!--      <el-form-item label="过期时间" prop="expireTime">-->
<!--        <el-date-picker clearable-->
<!--          v-model="queryParams.expireTime"-->
<!--          type="date"-->
<!--          value-format="yyyy-MM-dd"-->
<!--          placeholder="请选择过期时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
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
        >生成激活码</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['system:code:edit']"-->
<!--        >修改</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['system:code:remove']"-->
<!--        >删除</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['system:code:export']"-->
<!--        >导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="codeList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="激活码" align="center" prop="code" />
<!--      <el-table-column label="绑定用户ID" align="center" prop="userId" />-->
      <el-table-column label="Telegram用户ID" align="center" prop="telegramId" />
<!--      <el-table-column label="状态" align="center" prop="status" />-->
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_activation_code_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['system:code:edit']"-->
<!--          >修改</el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['system:code:remove']"-->
<!--          >删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改系统激活码对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="激活码" prop="code">
          <el-input v-model="form.code" placeholder="请输入激活码" />
        </el-form-item>
<!--        <el-form-item label="绑定用户ID" prop="userId">-->
<!--          <el-input v-model="form.userId" placeholder="请输入绑定用户ID" />-->
<!--        </el-form-item>-->
        <el-form-item label="Telegram用户ID" prop="telegramId">
          <el-input v-model="form.telegramId" placeholder="请输入Telegram用户ID" disabled />
        </el-form-item>
<!--        <el-form-item label="过期时间" prop="expireTime">-->
<!--          <el-date-picker clearable-->
<!--            v-model="form.expireTime"-->
<!--            type="date"-->
<!--            value-format="yyyy-MM-dd"-->
<!--            placeholder="请选择过期时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="备注" prop="remark">-->
<!--          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />-->
<!--        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listCode, getCode, delCode, addCode, updateCode, addActivationCode} from "@/api/system/code"

export default {
  name: "Code",
  dicts: ['sys_activation_code_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 系统激活码表格数据
      codeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        userId: null,
        telegramId: null,
        status: null,
        expireTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "激活码不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询系统激活码列表 */
    getList() {
      this.loading = true
      listCode(this.queryParams).then(response => {
        this.codeList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        code: null,
        userId: null,
        telegramId: null,
        status: null,
        expireTime: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      addActivationCode().then(response => {
        this.$modal.msgSuccess("新增成功")
        this.getList()
      })
      // this.reset()
      // this.open = true
      // this.title = "添加系统激活码"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCode(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改系统激活码"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCode(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCode(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除系统激活码编号为"' + ids + '"的数据项？').then(function() {
        return delCode(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/code/export', {
        ...this.queryParams
      }, `code_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
