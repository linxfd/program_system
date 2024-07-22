package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.dto.AssginMenuDto;
import com.program.model.entity.UserRole;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
    //根据角色id删除角色菜单关系数据
    void deleteByRoleId(Long roleId);

    //给角色分配菜单权限
    void doAssign(AssginMenuDto assginMenuDto);
}
