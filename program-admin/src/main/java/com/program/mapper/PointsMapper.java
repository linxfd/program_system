package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.Points;
import com.program.model.entity.SysMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linxf
 * @date 2024/8/18
 */
@Repository
public interface PointsMapper extends BaseMapper<Points> {

    // 查询所有菜单
    List<Points> selectPointsList(Points points);

}
