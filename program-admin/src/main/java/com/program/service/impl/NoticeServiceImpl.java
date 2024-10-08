package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.annotation.Cache;
import com.program.model.entity.Notice;
import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.mapper.NoticeMapper;
import com.program.service.NoticeService;
import com.program.utils.CommonUtils;
import com.program.utils.RedisUtil;
import com.program.model.vo.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final NoticeMapper noticeMapper;

    private final RedisUtil redisUtil;

    @Override
    public boolean setAllNoticeIsHistoryNotice() {
        return noticeMapper.setAllNoticeIsHistoryNotice();
    }

    @Cache(prefix = "cache:currentNewNotice", ttl = 10, randomTime = 2, timeUnit = TimeUnit.HOURS)
    @Override
    public String getCurrentNotice() {
        return noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1")).getContent();
    }

    @Override
    public PageResponse<Notice> getAllNotices(String content, Integer pageNo, Integer pageSize) {
        IPage<Notice> noticeIPage = new Page<>(pageNo, pageSize);
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        CommonUtils.setLikeWrapper(wrapper, Collections.singletonMap("content", content));
        wrapper.orderByDesc("status","update_time");

        noticeIPage = noticeMapper.selectPage(noticeIPage, wrapper);

        return PageResponse.<Notice>builder().data(noticeIPage.getRecords()).total(noticeIPage.getTotal()).build();
    }

    @Override
    @Transactional
    @Cache(prefix = "cache:currentNewNotice", ttl = 10, randomTime = 2, timeUnit = TimeUnit.HOURS, resetCache = true)
    public String publishNotice(Notice notice) {
        if (notice.getStatus() == 1) {//  当前发布的是置顶公告
            //  1. 将当前所有公告设置为历史公告
            setAllNoticeIsHistoryNotice();
            //  2. 新增最新公告进去
            notice.setCreateTime(new Date());
            noticeMapper.insert(notice);
            return notice.getContent();
        } else if (notice.getStatus() == 0) {//  不发布最新公告
            notice.setCreateTime(new Date());
            noticeMapper.insert(notice);

            return noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1")).getContent();
        } else {
            throw new BusinessException(CommonErrorCode.E_300001);
        }
    }

    @Override
    public void deleteNoticeByIds(String noticeIds) {
        // 转换成数组 需要操作的用户的id数组
        String[] ids = noticeIds.split(",");
        Notice currentNotice = noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1"));
        for (String id : ids) {
            //  如果当前公告id与需要操作的id相同，则跳过,不删除当前公告
            if (currentNotice.getNId().equals(Integer.parseInt(id))) {
                continue;
            }
            noticeMapper.deleteById(Integer.parseInt(id));
        }
    }

    @Override
    @Cache(prefix = "cache:currentNewNotice", ttl = 10, randomTime = 3, timeUnit = TimeUnit.HOURS, resetCache = true)
    public String updateNotice(Notice notice) {
        //  查询当前公告信息
        QueryWrapper<Notice> wrapper = new QueryWrapper<Notice>().eq("n_id", notice.getNId());
        Notice targetNotice = noticeMapper.selectOne(wrapper);

        if (notice.getStatus() == 1) {//  当前更新成为置顶公告
            //  将当前所有公告设置为历史公告
            setAllNoticeIsHistoryNotice();
            targetNotice.setUpdateTime(new Date());
            targetNotice.setContent(notice.getContent());
            targetNotice.setStatus(notice.getStatus());

            noticeMapper.update(targetNotice, wrapper);
            //  返回数据，更新缓存
            return targetNotice.getContent();
        } else if (notice.getStatus() == 0) { //  不发布最新公告
            targetNotice.setUpdateTime(new Date());
            targetNotice.setContent(notice.getContent());
            targetNotice.setStatus(notice.getStatus());
            noticeMapper.update(targetNotice, wrapper);

            //  返回数据，更新缓存
            return noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1")).getContent();
        } else {
            throw new BusinessException(CommonErrorCode.E_300002);
        }
    }
}
