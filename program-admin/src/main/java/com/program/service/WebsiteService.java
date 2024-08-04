package com.program.service;

import com.program.model.entity.Website;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.vo.PageResponse;
import com.program.model.dto.WebsiteDto;

import java.util.List;

/**
 * @author lxf
 * @date 2024-07-31
 */
public interface WebsiteService extends IService<Website>{

    // 分页查询
    PageResponse<Website> pageList(WebsiteDto website);

    // 添加
    void saveWebsite(Website website);

    // 查询分类下的网站
    List<Website> pageUserList(Integer classificationId);
}
