package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.Points;
import com.program.model.entity.User;

/**
 * @author linxf
 * @date 2024/8/18
 */
public interface PointsService extends IService<Points> {
    //为用户添加积分并记录在积分明细表中
    void addPoint(User user, int pointsCount);

    //获取用户累计签到积分
    int getAccumulatedSignCount(Integer id);
}
