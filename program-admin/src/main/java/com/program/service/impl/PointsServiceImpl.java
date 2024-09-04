package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.PointsMapper;
import com.program.mapper.UserMapper;
import com.program.model.dict.DictPoints;
import com.program.model.dto.PointsDto;
import com.program.model.entity.Points;
import com.program.model.entity.User;
import com.program.model.entity.Website;
import com.program.model.vo.PageResponse;
import com.program.service.PointsService;
import com.program.utils.EmptyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.program.utils.CommonUtils.setLikeWrapper;

/**
 * @author linxf
 * @date 2024/8/18
 */

@Service
@RequiredArgsConstructor
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {

    @Autowired
    private  PointsMapper pointsMapper;
    @Autowired
    private  UserMapper userMapper;

    @Override
    @Transactional
    public void addPoint(User user, int pointsCount) {
        // 更新积分
        user.setPoints(user.getPoints() + pointsCount);
        userMapper.updateById(user);
        // 添加积分流水
        Points points = new Points();
        points.setUserId(user.getId());
        points.setUsername(user.getUsername());
        points.setPointsFlow(pointsCount);
        points.setNotes("签到");
        points.setObtainMethod(DictPoints.METHOD_SIGN);
        points.setCreateTime(new Date());
        pointsMapper.insert(points);
    }

    @Override
    public int getAccumulatedSignCount(Integer id) {
        QueryWrapper<Points> qwrapper = new QueryWrapper<>();
        qwrapper.eq("user_id", id);
        qwrapper.eq("obtain_method", DictPoints.METHOD_SIGN);
        List<Points> points = null;
        try {
            points = pointsMapper.selectList(qwrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int sum = points.stream().mapToInt(Points::getPointsFlow).sum();
        return sum;
    }

    @Override
    public PageResponse<Points> pageList(PointsDto pointsDto) {
        IPage<Points> pointsPage = new Page<>(pointsDto.getPageNum(), pointsDto.getPageSize());

        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("points_flow", pointsDto.getPointsFlow());

        QueryWrapper<Points> pointsWrapper = new QueryWrapper<>();
        // 模糊查询
        setLikeWrapper(pointsWrapper, queryParams);
        if (!EmptyUtil.isNotUtils(pointsDto.getUsername())) {

            pointsWrapper.like("username", pointsDto.getUsername());
        }
        if (!EmptyUtil.isNotUtils(pointsDto.getNotes())) {
            pointsWrapper.like("notes", pointsDto.getNotes());
        }

        // 查询未删除的数据
        pointsWrapper.eq("is_deleted", 0);

        pointsPage = pointsMapper.selectPage(pointsPage, pointsWrapper);
        //这个是插件的分页的数据
        List<Points> records = pointsPage.getRecords();

        // 查询总points
        List<Points> pointsList = pointsMapper.selectPointsList(pointsDto);

        HashMap<String, Object> map = new HashMap<>();
        int pointsFlowSum = pointsList.stream()
                .mapToInt(points -> points.getPointsFlow()) // 将Points对象映射为int值（即pointsFlow）
                .sum(); // 计算总和
        map.put("pointsFlowSum", pointsFlowSum);
        return PageResponse.<Points>builder()
                .data(records)
                .map(map)
                .total(pointsPage.getTotal()).build();
    }


}
