package com.program.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.annotation.Cache;
import com.program.mapper.WebsiteMapper;
import com.program.model.entity.Website;
import com.program.model.vo.PageResponse;
import com.program.model.dto.WebsiteDto;
import com.program.service.WebsiteService;
import com.program.utils.EmptyUtil;
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
    public PageResponse<Website> pageList(WebsiteDto websiteDto) {
        IPage<Website> websitePage = new Page<>(websiteDto.getPageNum(), websiteDto.getPageSize());

        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("notes", websiteDto.getNotes());
        queryParams.put("name", websiteDto.getName());
        queryParams.put("url", websiteDto.getUrl());
        QueryWrapper<Website> wrapper = new QueryWrapper<>();
        // 模糊查询
        setLikeWrapper(wrapper, queryParams);
        if (!EmptyUtil.isNotUtils(websiteDto.getClassificationId())) {
            wrapper.eq("classification_id", websiteDto.getClassificationId());
        }

        // 查询未删除的数据
        wrapper.eq("is_deleted", 0);
        // 排序
        wrapper.orderByDesc("sort_value");
        wrapper.orderByAsc( "classification_id");

        websitePage = websiteMapper.selectPage(websitePage, wrapper);
        List<Website> records = websitePage.getRecords();

        return PageResponse.<Website>builder().data(records).total(websitePage.getTotal()).build();
    }

    @Override
    public void saveWebsite(Website website) {
//        if(EmptyUtil.isNotUtils(website.getIcon())){
//            website.setIcon(website.getUrl()+"/favicon.ico");
//        }
        websiteMapper.insert(website);
    }

    @Override
    @Cache(prefix = "cache:website", suffix = "#classificationId",ttl = 10, randomTime = 10, timeUnit = TimeUnit.DAYS)
    public List<Website> pageUserList(Integer classificationId) {

        QueryWrapper<Website> wrapper = new QueryWrapper<>();
        if (!EmptyUtil.isNotUtils(classificationId)) {
            wrapper.eq("classification_id", classificationId);
        }
        // 查询未删除的数据
        wrapper.eq("is_deleted", 0);
        // 排序
        wrapper.orderByDesc("sort_value");
        wrapper.orderByAsc( "classification_id");

        List<Website> websites = websiteMapper.selectList(wrapper);
        return websites;
    }
}
