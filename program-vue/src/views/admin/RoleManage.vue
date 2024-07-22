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

        <el-table-column prop="roleId" label="角色ID">
        </el-table-column>

        <el-table-column prop="roleName" label="角色名称">
        </el-table-column>
        <el-table-column prop="roleInfo" label="角色信息">
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
      this.roleId = value.roleId
      sysMenu.GetSysRoleMenuIds(value.roleId).then((resp)=>{
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
        roleId: this.roleId,
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
    editShow(){

    },
    //删除
    remove(){

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
