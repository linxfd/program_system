package com.program.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.QuestionBankMapper;
import com.program.mapper.WebsiteClassificationMapper;
import com.program.mapper.WebsiteMapper;
import com.program.model.dict.IsDeleted;
import com.program.model.entity.QuestionBank;
import com.program.model.entity.Website;
import com.program.model.entity.WebsiteClassification;
import com.program.model.vo.PageResponse;
import com.program.model.vo.WebsiteClassificationVo;
import com.program.model.vo.WebsiteVo;
import com.program.service.QuestionBankService;
import com.program.service.WebsiteClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.program.utils.CommonUtils.setLikeWrapper;

/**
 * @author linxf
 * @date 2024/7/31
 */
@Service
public class WebsiteClassificationServiceImpl extends ServiceImpl<WebsiteClassificationMapper, WebsiteClassification> implements WebsiteClassificationService {

    @Autowired
    private WebsiteClassificationMapper websiteClassificationMapper;

    @Autowired
    private WebsiteMapper websiteMapper;

    @Override
    public PageResponse<WebsiteClassification> pageList(WebsiteClassificationVo websiteClassificationVo ) {
        IPage<WebsiteClassification> websiteClassificationPage = new Page<>(websiteClassificationVo.getPageNum(), websiteClassificationVo.getPageSize());

        // 查询条件
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("notes", websiteClassificationVo.getNotes());
        queryParams.put("name", websiteClassificationVo.getName());
        QueryWrapper<WebsiteClassification> wrapper = new QueryWrapper<>();
        // 模糊查询
        setLikeWrapper(wrapper, queryParams);
        // 查询未删除的数据
        wrapper.eq("is_deleted", 0);
        // 排序
        wrapper.orderByAsc( "name");
        wrapper.orderByDesc("create_time");

        websiteClassificationPage = websiteClassificationMapper.selectPage(websiteClassificationPage, wrapper);
        List<WebsiteClassification> records = websiteClassificationPage.getRecords();

        return PageResponse.<WebsiteClassification>builder().data(records).total(websiteClassificationPage.getTotal()).build();
    }

//    @Override
//    public List<String> getClassidied() {
//        // 查询所有分类
//        List<Website> websites = websiteMapper.selectList(new QueryWrapper<Website>().eq("is_deleted", IsDeleted.NOT_DELETED));
//        List<Integer> collect = websites.stream()
//                .map(Website::getClassificationId)
//                .collect(Collectors.toList());
//        List<WebsiteClassification> websiteClassifications = websiteClassificationMapper.selectBatchIds(collect);
//        List<String> strings = websiteClassifications.stream().map(WebsiteClassification::getName).sorted()
//                .collect(Collectors.toList());
//
//        return strings;
//    }
}

