<template>
  <el-container>
    <el-main>
      <el-button type="primary" style="margin:10px 10px 10px 10px" icon="el-icon-plus" @click="showAddDialog">添加角色</el-button>
      
      <el-table
        ref="roleForm"
        highlight-current-row
        :border="true"
        v-loading="loading"
        :data="roleInfo"
        tooltip-effect="dark"
        style="width: 100%">

        <el-table-column prop="id" label="角色ID">
        </el-table-column>

        <el-table-column prop="roleName" label="角色名称">
        </el-table-column>
        <el-table-column prop="roleInfo" label="角色信息">
        </el-table-column>

        <el-table-column align="center"
                         label="更新时间">
          <template slot-scope="scope">
            {{ scope.row.updateTime }}
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="280" #default="scope">
          <el-button type="success" size="small" @click="editMenu(scope.row)">
            分配权限
          </el-button>
          <el-button type="primary" size="small" @click="editShow(scope.row)">
            修改
          </el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="remove(scope.row)">
          </el-button>
        </el-table-column>
      </el-table>

      <!-- 分配菜单的对话框 
      // tree组件添加ref属性，后期方便进行tree组件对象的获取
      -->
      <el-dialog :visible.sync="dialogMenuVisible" title="分配菜单" width="40%">
        <el-form label-width="80px">
          <el-tree
            :data="sysMenuTreeList"
            ref="tree"
            show-checkbox
            default-expand-all
            :check-on-click-node="true"
            node-key="id"
            :props="defaultProps"
          />
          <el-form-item>
            <el-button type="primary" @click="doAssign">提交</el-button>
            <el-button @click="dialogMenuVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

     <!--添加/修改弹窗 -->
      <el-dialog :title="roleVisible" :visible.sync="addTableVisible" width="30%" @close="resetAddForm"
               center>

      <el-form :model="addForm"  ref="addForm">
          <el-form-item label="角色名称" label-width="120px" prop="username">
            <el-input v-model="addForm.roleName"></el-input>
          </el-form-item>
          <el-form-item label="角色信息" label-width="120px" prop="username">
            <el-input v-model="addForm.roleInfo"></el-input>
          </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="addTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">确 定</el-button>
      </div>
    </el-dialog>

    </el-main>
  </el-container>

</template>

<script>
import role from '@/api/role'
import sysMenu from '@/api/sysMenu'

export default {
  name: 'RoleManage',
  data () {
    return {
      //角色信息
      roleInfo: [],
      //表格数据加载
      loading: true,
      //弹窗显示
      dialogMenuVisible:false,
      //树形结构数据
      sysMenuTreeList:[],
      //树形结构属性
      defaultProps :{
        children: 'children',
        label: 'topMenuName',
      },
      // 树对象变量
      tree:[],
      //角色id
      roleId:'',
      //添加表单
      addForm: {
        'id':'',
        'roleName': '',
        'roleInfo': '',
      },
      //添加表单验证规则
      addTableVisible:false,
      roleVisible:'添加角色',
    }
  },
  created () {

    this.getRoleInfo()
  },
  methods: {
    //获取用户角色信息
    getRoleInfo () {
      role.getRoleInfo().then((resp) => {
        if (resp.code === 200) {
          this.roleInfo = resp.data
          this.loading = false
        } else {
          this.$notify({
            title: 'Tips',
            type: 'error',
            message: '获取信息失败',
            duration: 2000
          })
        }
      })
    },
    //分配菜单
    editMenu(value){
      this.dialogMenuVisible = true;
      //角色id
      this.roleId = value.id
      sysMenu.GetSysRoleMenuIds(value.id).then((resp)=>{
        if (resp.code === 200) {
            //所有数据
            this.sysMenuTreeList = resp.data.sysMenuList
            //勾选的数据
            this.$refs.tree.setCheckedKeys(resp.data.roleMenuIds) 
          }
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000,
            });
      })
    },
    //添加
    showAddDialog(){
      this.addForm={};
      this.roleVisible='添加角色'
      this.addTableVisible=true;
    },
    //提交
    doAssign(){
      const checkedNodes = this.$refs.tree.getCheckedNodes() // 获取选中的节点
      const checkedNodesIds = checkedNodes.map(node => {
        // 获取选中的节点的id
        return {
          id: node.id,
          isHalf: 0,
        }
      })

      // 获取半选中的节点数据，当一个节点的子节点被部分选中时，该节点会呈现出半选中的状态
      const halfCheckedNodes = this.$refs.tree.getHalfCheckedNodes()
      const halfCheckedNodesIds = halfCheckedNodes.map(node => {
        // 获取半选中节点的id
        return {
          id: node.id,
          isHalf: 1,
        }
      })

      // 将选中的节点id和半选中的节点的id进行合并
      const menuIds = [...checkedNodesIds, ...halfCheckedNodesIds]
      console.log(menuIds)

      // 构建请求数据
      const assignMenuDto = {
        id: this.roleId,
        menuIdList: menuIds,
      }

      // 发送请求
      role.DoAssignMenuIdToSysRole(assignMenuDto).then((resp)=>{
        if(resp.code === 200){
            this.$notify({
              title: 'Tips',
              message: resp.message,
              type: 'success',
              duration: 2000,
            });
        }
      })
      this.dialogMenuVisible = false
    },
    //修改
    editShow(row){
      this.addForm={...row}
      this.roleVisible='修改角色'
      this.addTableVisible=true;
    },
    //提交
    addUser () {
      if(this.addForm.id){
        role.updateRole(this.addForm).then((resp)=>{
          if (resp.code === 200) {
              this.getRoleInfo()
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'error',
                duration: 2000
              })
            }
            this.addTableVisible = false
        })
      }else{
          role.addRole(this.addForm).then((resp) => {
            if (resp.code === 200) {
              this.getRoleInfo()
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'error',
                duration: 2000
              })
            }
            this.addTableVisible = false
          })
      }
      
    },
    //删除
    remove(val){
      this.$confirm(`此操作将永久删除"${val.roleName}"用户, 是否继续?`, '注意', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          role.deleteRole(val.id).then((resp)=>{
            if(resp.code === 200){
              this.$notify({
                  title: 'Tips',
                  message: resp.message,
                  type: 'success',
                  duration: 2000
                })
              this.getRoleInfo()
            }else {
              this.$notify({
                title: 'Tips',
                message: resp.message,
                type: 'error',
                duration: 2000,
              });
            }
          })
        });
    },
    //表单信息重置
    resetAddForm () {
      //清空表格数据
      this.$refs['addForm'].resetFields()
    },

  }
}
</script>

<style scoped lang="scss">
.el-container {
  animation: leftMoveIn .7s ease-in;
}

@keyframes leftMoveIn {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0%);
    opacity: 1;
  }
}
</style>
