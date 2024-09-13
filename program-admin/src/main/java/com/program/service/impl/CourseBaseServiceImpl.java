package com.program.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.mapper.CourseBaseMapper;
import com.program.mapper.CourseUnitMapper;
import com.program.model.dict.DictConstant;
import com.program.model.dict.DictStatus;
import com.program.model.dto.CourseBaseDto;
import com.program.model.dto.CourseDto;
import com.program.model.dto.UserDto;
import com.program.model.entity.CourseBase;
import com.program.model.entity.CourseUnit;
import com.program.model.entity.User;
import com.program.model.vo.*;
import com.program.service.CourseBaseService;
import com.program.utils.EmptyUtil;
import com.program.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.program.utils.CommonUtils.setLikeWrapper;


/**
 * @author lxf
 * @date 2024-09-05
 */
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseUnitMapper courseUnitMapper;


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

    @Override
    @Transactional
    public void addCategory(CourseDto courseDto, HttpServletRequest request) {
        // 添加课程基础信息
        CourseBase courseBase = courseDto.getCourseBase();
        TokenVo userInfoByToken = JwtUtils.getUserInfoByToken(request);
        courseBase.setCreatePerson(userInfoByToken.getUsername());
        courseBase.setCourseStatus(DictStatus.UNPUBLISHED);
        courseBaseMapper.insert(courseBase);

        List<CourseUnit> cards = courseDto.getCards();
        for (int i = 0; i < cards.size(); i++) {
            CourseUnit courseUnit = cards.get(i);
            if (EmptyUtil.isEmpty(courseUnit.getUrl())){
                continue;
            }
            courseUnit.setCourseId(courseBase.getId());
            courseUnit.setSortValue(i);
            courseUnitMapper.insert(courseUnit);
        }

    }

    @Override
    public List<CourseUnitVo> queryAudit(Integer id) {
        return courseUnitMapper.queryAudit(id);
    }

    @Override
    public CourseDto getCategoryInfo(Integer id) {
        CourseBase courseBase = courseBaseMapper.selectById(id);
        QueryWrapper<CourseUnit> qw = new QueryWrapper<>();
        qw.eq("course_id", id);
        List<CourseUnit> courseUnits = courseUnitMapper.selectList(qw);
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseBase(courseBase);
        courseDto.setCards(courseUnits);
        return courseDto;
    }

    @Override
    @Transactional
    public void updateCourse(CourseDto courseDto, HttpServletRequest request) {
        // 修改课程基础信息
        CourseBase courseBase = courseDto.getCourseBase();
        courseBaseMapper.updateById(courseBase);
        // 修改课程视频信息
        courseUnitMapper.delete(new QueryWrapper<CourseUnit>().eq("course_id", courseDto.getCourseBase().getId()));
        List<CourseUnit> cards = courseDto.getCards();
        for (int i = 0; i < cards.size(); i++) {
            CourseUnit courseUnit = cards.get(i);
            if (EmptyUtil.isEmpty(courseUnit.getUrl())){
                continue;
            }
            courseUnit.setCourseId(courseBase.getId());
            courseUnit.setSortValue(i);
            courseUnitMapper.insert(courseUnit);
        }
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        // 删除课程基础信息
        courseBaseMapper.deleteById(id);
        // 删除课程视频信息
        courseUnitMapper.delete(new QueryWrapper<CourseUnit>().eq("course_id", id));
    }

    @Override
    public void handle(Integer type, String ids) {
        // 转换成数组 需要操作的用户的id数组
        String[] list = ids.split(",");
        switch (type) {
            case 3:
                for (String id : list) {
                    this.deleteCourse(Integer.parseInt(id));
                }
                break;
            default:
                throw new BusinessException(CommonErrorCode.E_100105);
        }
    }

    @Override
    public CommonResult announce(Integer id) {
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (DictStatus.UNPUBLISHED.equals(courseBase.getCourseStatus())){
            List<CourseUnitVo> courseUnits = courseUnitMapper.auditStatusNumber(id);
            if (courseUnits.size() <=0 ){
                return CommonResult.<Boolean>builder()
                        .data(false)
                        .message("失败，课程中的视频单元至少需要一个通过审核")
                        .build();
            }
        }
        courseBase.setCourseStatus(courseBase.getCourseStatus() == 1 ? 2 : 1);
        courseBaseMapper.updateById(courseBase);
        return CommonResult.<Boolean>builder()
                .data(true)
                .message(courseBase.getCourseStatus() == 1 ? "成功发布" : "成功下架")
                .build();
    }

    @Override
    public CourseInfoVO getCourseInfo(Integer id) {
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        // 查询课程基础信息
        CourseBase courseBase = courseBaseMapper.selectById(id);
        if (EmptyUtil.isEmpty(courseBase)){
            throw new BusinessException(CommonErrorCode.E_100105);
        }
        courseInfoVO.setCourseBase(courseBase);
        // 查询课程视频信息
        List<CourseUnitVo> courseUnits = courseUnitMapper.auditStatusNumber(id);
        courseInfoVO.setCourseUnits(courseUnits);
        return courseInfoVO;
    }
}
