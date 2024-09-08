package com.program.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.mapper.PointsMapper;
import com.program.mapper.UserMapper;
import com.program.model.entity.User;
import com.program.model.vo.TokenVo;
import com.program.service.PointsService;
import com.program.service.SignService;
import com.program.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author linxf
 * @date 2024/8/17
 */
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

    private final UserMapper userMapper;

    private final RedisUtil redisUtil;

    @Autowired
    private PointsService pointsService;


    /**
     * 统计连续签到的次数
     * @param userId
     * @param date
     * @return
     */
    public  int getContinuousSignCount(Integer userId, Date date) {
        // 获取日期对应的天数，多少号，假设是 30
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 构建 Key
        String signKey =String.format("user:sign:%d:%s", userId,
                DateUtil.format(date, "yyyyMM"));
        // bitfield user:sign:5:202212 30 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisUtil.bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int signCount = 0;
        long v = list.get(0) == null ? 0 : list.get(0);
        // i 表示位移操作次数
        for (int i = dayOfMonth; i > 0; i--) {
            // 右移再左移，如果等于自己说明最低位是 0，表示未签到
            if (v >> 1 << 1 == v) {
                // 低位 0 且非当天说明连续签到中断了
                if (i != dayOfMonth) {
                    break;
                }
            } else {
                signCount++;
            }
            // 右移一位并重新赋值，相当于把最低位丢弃一位
            v >>= 1;
        }
        return signCount;
    }

    @Override
    public Map<String, Object> doSign(String dateStr, HttpServletRequest request) {
        // 获取登录用户信息
        TokenVo userInfo  = JwtUtils.getUserInfoByToken(request);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userInfo.getUsername()));
        // 获取日期
        Date date = DateMyUtil.getDate(dateStr);
        // 获取日期对应的天数，多少号( 从 0 开始，0就代表1号)
        int offset = DateUtil.dayOfMonth(date) - 1;
        // 构建 Key user:sign:5:yyyyMM
        String signKey = String.format("user:sign:%d:%s",
                user.getId(),DateUtil.format(date, "yyyyMM"));

        // 查看是否已签到
        boolean isSigned = redisUtil.get(signKey, offset);
        // 统计连续签到的次数
        int count = this.getContinuousSignCount(user.getId(), date);
        if (!isSigned){
            count ++;
        }
        // 计算积分
        int pointsCount = PointsUtil.calculatePoints(count);

        // 计算下一次的积分
        int nextPointsCount = PointsUtil.calculatePoints(count + 1);

        // 判断是否已签到
        if (!isSigned){
            // 签到
            redisUtil.set(signKey, offset,true);
            // 为用户添加积分并记录在积分明细表中
            pointsService.addPoint(user,pointsCount);
        }

        // 累计签到积分
        int accumulatedSignCount = pointsService.getAccumulatedSignCount(user.getId());

        Map<String, Object> map = new HashMap<>();
        // 连续签到次数
        map.put("count", EmptyUtil.isNotEmpty(count) ? count : 1);
        // 本次签到积分
        map.put("pointsCount", pointsCount);
        // 下一次签到积分
        map.put("nextPointsCount", nextPointsCount);
        // 是否已签到
        map.put("isSigned", isSigned);
        // 累计签到积分
        map.put("accumulatedSignCount", accumulatedSignCount);
        return map;
    }

    /**
     * 获取用户签到次数
     *
     * @param dateStr
     * @return
     */
    public Long getSignCount(String dateStr, HttpServletRequest request) {
        // 获取登录用户信息
        TokenVo userInfo  = JwtUtils.getUserInfoByToken(request);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userInfo.getUsername()));
        // 获取日期
        Date date = DateMyUtil.getDate(dateStr);
        // 构建 Key
        String signKey = String.format("user:sign:%d:%s",
                user.getId(),DateUtil.format(date, "yyyyMM"));
        // e.g. BITCOUNT user:sign:6:202212
        return redisUtil.execute(signKey);
    }

    /**
     * 获取当月签到情况
     * @param dateStr
     * @return
     */
    public Map<String, Boolean> getSignInfo(String dateStr,HttpServletRequest request) {
        // 获取登录用户信息
        TokenVo userInfo  = JwtUtils.getUserInfoByToken(request);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userInfo.getUsername()));
        // 获取日期
        Date date = DateMyUtil.getDate(dateStr);
        // 构建 Key
        String signKey = String.format("user:sign:%d:%s",
                user.getId(),DateUtil.format(date, "yyyyMM"));
        // 构建一个自动排序的 Map
        Map<String, Boolean> signInfo = new TreeMap<>();
        // 获取某月的总天数（考虑闰年）
        int dayOfMonth = DateUtil.lengthOfMonth(DateUtil.month(date) + 1,
                DateUtil.isLeapYear(DateUtil.dayOfYear(date)));
        // bitfield user:sign:5:202011 u30 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisUtil.bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return signInfo;
        }
        long v = list.get(0) == null ? 0 : list.get(0);
        // 从低位到高位进行遍历，为 0 表示未签到，为 1 表示已签到
        for (int i = dayOfMonth; i > 0; i--) {
            /*
                签到：  yyyy-MM-01 true
                未签到：yyyy-MM-01 false
             */
            LocalDateTime localDateTime = LocalDateTimeUtil.of(date).withDayOfMonth(i);
            boolean flag = v >> 1 << 1 != v;
            signInfo.put(DateUtil.format(localDateTime, "yyyy-MM-dd"), flag);
            v >>= 1;
        }
        return signInfo;
    }


}
