package com.program.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.program.model.entity.WebsiteClassification;
import com.program.model.vo.CommonResult;
import com.program.model.vo.PageResponse;
import com.program.model.vo.WebsiteClassificationVo;
import com.program.model.vo.WebsiteVo;

import java.util.List;


/**
 * @author linxf
 * @date 2024/7/31
 */
public interface WebsiteClassificationService extends IService<WebsiteClassification> {
    // 分页查询
    PageResponse<WebsiteClassification> pageList(WebsiteClassificationVo websiteClassificationVo);

    // 删除
    CommonResult removeClassification(Integer[] ids);

//    // 获得网站分类名称
//    List<String> getClassidied();

}
