package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.Points;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linxf
 * @date 2024/8/18
 */
@Repository
public interface PointsMapper extends BaseMapper<Points> {

    List<Points> selectList(Points points);
}
