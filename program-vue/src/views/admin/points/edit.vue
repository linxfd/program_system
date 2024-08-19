<template>
  <div class="cmain">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户账户" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户账户" clearable/>
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" clearable/>
        </el-form-item>
        <el-form-item label="积分流水，如20，-23" prop="pointsFlow">
          <el-input v-model="form.pointsFlow" placeholder="请输入积分流水，如20，-23" clearable/>
        </el-form-item>
        <el-form-item label="积分备注，如签到加+2" prop="notes">
          <el-input v-model="form.notes" placeholder="请输入积分备注，如签到加+2" clearable/>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-date-picker clearable
            v-model="form.createTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="更新时间" prop="updateTime">
          <el-date-picker clearable
            v-model="form.updateTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择更新时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="删除标记" prop="isDeleted">
          <el-input v-model="form.isDeleted" placeholder="请输入删除标记" clearable/>
        </el-form-item>
      </el-form>
    <div class="mfooter">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="$layer.close(layerid)">取 消</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {},
      rules: {
        username: [
          { required: true, message: "用户账户不能为空", trigger: "blur" }
        ],
        notes: [
          { required: true, message: "积分备注，如签到加+2不能为空", trigger: "blur" }
        ],
        obtainMethod: [
          { required: true, message: "获取方式，0其他，1签到，2考试不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        isDeleted: [
          { required: true, message: "删除标记不能为空", trigger: "blur" }
        ]
      }
    };
  },
  props: {
    param: {
      type: Object,
      default: () => {
        return {};
      }
    },
    layerid: {
      type: String
    }
  },
  mounted() {
    if (this.param.id) {
      this.ajax({ url: '/work/points/detail/' + this.param.id }).then(response => {
        this.form = response.data;
      });
    }
  },
  methods: {
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id) {
              this.ajax({method: 'post',url: '/work/points/edit', data: this.form }).then(response => {
                  this.$modal.msgSuccess("修改成功");
                  this.$layer.close(this.layerid);
                  this.$parent.getList();
              });
          } else {
              this.ajax({method: 'post',url: '/work/points/add', data: this.form }).then(response => {
                  this.$modal.msgSuccess("新增成功");
                  this.$layer.close(this.layerid);
                  this.$parent.getList();
               });
          }
        }
      });
    }
  }
};
</script>
