package com.program.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.entity.Notice;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

    // 将所有公告设置为历史公告
    boolean setAllNoticeIsHistoryNotice();

}
