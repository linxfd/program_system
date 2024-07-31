package com.program.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.mapper.WebsiteMapper;
import com.program.model.dict.IsDeleted;
import com.program.model.entity.User;
import com.program.model.entity.Website;
import com.program.model.vo.PageResponse;
import com.program.model.vo.UserInfoVo;
import com.program.model.vo.WebsiteVo;
import com.program.service.WebsiteService;
import com.program.utils.NotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import static com.program.utils.CommonUtils.setLikeWrapper;

/**
 * @author lxf
 * @date 2024-07-31
 */
@Service
public class WebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> implements WebsiteService {
    @Autowired
    private WebsiteMapper websiteMapper;



    @Override
    public PageResponse<Website> pageList(WebsiteVo websiteVo) {
        IPage<Website> websitePage = new Page<>(websiteVo.getPageNum(), websiteVo.getPageSize());

        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("notes", websiteVo.getNotes());
        queryParams.put("name", websiteVo.getName());
        queryParams.put("url", websiteVo.getUrl());
        QueryWrapper<Website> wrapper = new QueryWrapper<>();
        // 模糊查询
        setLikeWrapper(wrapper, queryParams);
        if (!NotUtils.isNotUtils(websiteVo.getClassificationId())) {
            wrapper.eq("classification_id", websiteVo.getClassificationId());
        }

        // 查询未删除的数据
        wrapper.eq("is_deleted", 0);
        // 排序
        wrapper.orderByAsc( "name");
        wrapper.orderByDesc("create_time");

        websitePage = websiteMapper.selectPage(websitePage, wrapper);
        List<Website> records = websitePage.getRecords();

        return PageResponse.<Website>builder().data(records).total(websitePage.getTotal()).build();
    }

    @Override
    public void saveWebsite(Website website) {
        if(NotUtils.isNotUtils(website.getIcon())){
            website.setIcon(website.getUrl()+"/favicon.ico");
        }
        websiteMapper.insert(website);
    }


}
