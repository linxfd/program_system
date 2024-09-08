package com.program.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.CourseBaseMapper;
import com.program.model.dto.CourseBaseDto;
import com.program.model.dto.UserDto;
import com.program.model.entity.CourseBase;
import com.program.model.entity.User;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
import com.program.service.CourseBaseService;
import com.program.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.program.utils.CommonUtils.setLikeWrapper;


/**
 * @author lxf
 * @date 2024-09-05
 */
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    @Autowired
    private CourseBaseMapper courseBaseMapper;


    @Override
    public PageResponse<CourseBase> list(CourseBaseDto courseBaseDto) {
        if(EmptyUtil.isNotUtils(courseBaseDto.getPageNo())
                ||EmptyUtil.isNotUtils(courseBaseDto.getPageSize())){
            courseBaseDto.setPageNo(1);
            courseBaseDto.setPageSize(10);
        }
        IPage<CourseBase> courseBasePage = new Page<>(courseBaseDto.getPageNo(), courseBaseDto.getPageSize());
        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", courseBaseDto.getName());
        queryParams.put("description", courseBaseDto.getDescription());

        QueryWrapper<CourseBase> wrapper = new QueryWrapper<>();
        setLikeWrapper(wrapper, queryParams);

        wrapper.eq("is_deleted", 0);
        if (EmptyUtil.isNotEmpty(courseBaseDto.getMt())){
            wrapper.eq("mt",courseBaseDto.getMt());
        }
        if (EmptyUtil.isNotEmpty(courseBaseDto.getSt())){
            wrapper.eq("st",courseBaseDto.getSt());
        }

        wrapper.orderByAsc("create_time");

        courseBasePage = courseBaseMapper.selectPage(courseBasePage, wrapper);
        List<CourseBase> records = courseBasePage.getRecords();

        return PageResponse.<CourseBase>builder().data(records).total(courseBasePage.getTotal()).build();
    }

}
