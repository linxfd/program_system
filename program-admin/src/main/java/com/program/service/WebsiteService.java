package com.program.service;

import java.util.List;
import com.program.model.entity.Website;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.vo.PageResponse;
import com.program.model.vo.WebsiteVo;

/**
 * @author lxf
 * @date 2024-07-31
 */
public interface WebsiteService extends IService<Website>{

    // 分页查询
    PageResponse<Website> pageList(WebsiteVo website);

    // 添加
    void saveWebsite(Website website);



}
