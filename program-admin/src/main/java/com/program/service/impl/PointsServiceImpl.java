package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.PointsMapper;
import com.program.mapper.UserMapper;
import com.program.model.dict.DictPoints;
import com.program.model.entity.Points;
import com.program.model.entity.User;
import com.program.service.PointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
        List<Points> points = pointsMapper.selectList(qwrapper);
        int sum = points.stream().mapToInt(Points::getPointsFlow).sum();
        return sum;
    }
}
