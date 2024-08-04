package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.Notice;
import com.program.model.vo.PageResponse;


public interface NoticeService extends IService<Notice> {
    // 将所有公告设置为历史公告
    boolean setAllNoticeIsHistoryNotice();

    String getCurrentNotice();

    PageResponse<Notice> getAllNotices(String content, Integer pageNo, Integer pageSize);

    String publishNotice(Notice notice);

    // 批量删除公告
    void deleteNoticeByIds(String noticeIds);

    // 更新公告
    String  updateNotice(Notice notice);
}
