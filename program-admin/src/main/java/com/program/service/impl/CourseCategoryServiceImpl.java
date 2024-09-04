package com.program.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.mapper.CourseCategoryMapper;
import com.program.model.dict.IsDeleted;
import com.program.model.entity.CourseCategory;
import com.program.model.entity.SysMenu;
import com.program.service.CourseCategoryService;
import com.program.utils.EmptyUtil;
import com.program.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;

/**
 * @author lxf
 * @date 2024-09-04
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;



    @Override
    public List<CourseCategory> findNodes() {
        //查询所有菜单
        QueryWrapper<CourseCategory> wrapper = new QueryWrapper<CourseCategory>();
        wrapper.eq("is_deleted", IsDeleted.NOT_DELETED);
        List<CourseCategory> list = courseCategoryMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //构建树形数据
        List<CourseCategory> treeList = MenuHelper.buildCategoryTree(list);
        return treeList;
    }

    @Override
    public void saveCourseCategory(CourseCategory courseCategory) {
        if(EmptyUtil.isEmpty(courseCategory.getLabel())){
            courseCategory.setLabel(courseCategory.getName());
        }
        courseCategoryMapper.insert(courseCategory);
    }
}
