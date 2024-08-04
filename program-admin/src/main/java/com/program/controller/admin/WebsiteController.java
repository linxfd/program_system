package com.program.controller.admin;

import java.util.Arrays;
import java.util.List;

import com.program.annotation.Cache;
import com.program.model.dto.WebsiteDto;
import com.program.model.entity.Website;
import com.program.model.entity.WebsiteClassification;
import com.program.model.vo.*;
import com.program.service.WebsiteClassificationService;
import com.program.service.WebsiteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 网站
 * @author lxf
 * @date 2024-07-31
 */
@RestController
@RequestMapping("/admin/website")
public class WebsiteController  {
    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private WebsiteClassificationService websiteClassificationService;


    @ApiOperation(value = "网站详情")
    @GetMapping(value = "/detail/{id}")
    public CommonResult detail(@PathVariable("id") Integer id){

        return CommonResult.build(websiteService.getById(id), CommonResultEnum.SUCCESS_QUERY);
    }

    @ApiOperation(value = "获得网站列表")
    @PostMapping("/list")
    public CommonResult list(@RequestBody WebsiteDto websiteDto){
        PageResponse<Website> list = websiteService.pageList(websiteDto);
        return CommonResult.<PageResponse<Website>>builder()
                .data(list)
                .build();
    }

    @ApiOperation(value = "网站新增")
    @PostMapping("/add")
    @Cache(prefix = "cache:website",removeCache = true)
    public CommonResult add(@RequestBody Website website){
        websiteService.saveWebsite(website);
        return CommonResult.build(null, CommonResultEnum.SUCCESS_ADD);
    }

    @ApiOperation(value = "网站修改")
    @PostMapping("/edit")
    @Cache(prefix = "cache:website",removeCache = true)
    public CommonResult edit(@RequestBody Website website){
        boolean b = websiteService.updateById(website);
        return CommonResult.build(b, CommonResultEnum.SUCCESS_UPDATE);
    }

    @ApiOperation(value = "网站删除")
    @GetMapping("/remove/{ids}")
    @Cache(prefix = "cache:website",removeCache = true)
    public CommonResult remove(@PathVariable Integer[] ids){
        boolean b = websiteService.removeByIds(Arrays.asList(ids));
        return CommonResult.build(b, CommonResultEnum.SUCCESS_DELETE);
    }

    @ApiOperation(value = "网站分类新增")
    @PostMapping("/addClassification")
    public CommonResult addClassification(@RequestBody WebsiteClassification websiteClassification){
        websiteClassificationService.save(websiteClassification);
        return CommonResult.build(null, CommonResultEnum.SUCCESS_ADD);
    }

    @ApiOperation(value = "网站分类修改")
    @PostMapping("/editClassification")
    public CommonResult editClassification(@RequestBody WebsiteClassification websiteClassification){
        websiteClassificationService.updateById(websiteClassification);
        return CommonResult.build(null, CommonResultEnum.SUCCESS_QUERY);
    }
    @ApiOperation(value = "网站分类删除")
    @GetMapping("/removeClassification/{ids}")
    public CommonResult removeClassification(@PathVariable Integer[] ids){
//        boolean b = .removeByIds(Arrays.asList(ids));
        CommonResult listResult = websiteClassificationService.removeClassification(ids);
        return listResult;
    }


}
