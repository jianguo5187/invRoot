<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="12" :lg="6" style="padding-left: 20px">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleBatchAdd"
        >批量生成激活码</el-button>
      </el-col>

      <el-col :sm="12" :lg="6" style="padding-left: 20px">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >生成激活码</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {addActivationCode, batchAddActivationCode} from "@/api/system/code";

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.9.0"
    }
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank")
    },
     /** 批量新增按钮操作 */
     handleBatchAdd() {
      batchAddActivationCode().then(response => {
        this.$modal.msgSuccess("新增成功")
        this.$router.push({ path: "/system/code" });
        // this.getList()
      })
      // this.reset()
      // this.open = true
      // this.title = "添加系统激活码"
    },
    /** 新增按钮操作 */
    handleAdd() {
      addActivationCode().then(response => {
        this.$modal.msgSuccess("新增成功")
        this.$router.push({ path: "/system/code" });
        // this.getList()
      })
      // this.reset()
      // this.open = true
      // this.title = "添加系统激活码"
    },
  }
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

