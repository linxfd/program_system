package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.model.entity.CourseUnit;
import com.program.model.vo.CourseUnitVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linxf
 * @date 2024/9/9
 */
@Repository
public interface CourseUnitMapper extends BaseMapper<CourseUnit> {

    List<CourseUnitVo> queryAudit(Integer id);
}
