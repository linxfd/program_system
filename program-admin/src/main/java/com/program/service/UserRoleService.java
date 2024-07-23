package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.dto.AssginMenuDto;
import com.program.model.entity.UserRole;
import com.program.model.vo.CommonResult;

import java.util.List;


public interface UserRoleService extends IService<UserRole> {

    String getMenuInfo(Integer roleId);

    List<UserRole> getUserRole();

    //分配菜单
    void doAssign(AssginMenuDto assginMenuDto);

    //添加角色
    void addRole(UserRole userRole);

    //修改角色
    void updateRole(UserRole userRole);

    //删除角色
    CommonResult deleteRole(Integer id);
}
