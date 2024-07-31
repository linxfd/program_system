package com.program.controller.admin;

import java.util.Arrays;
import java.util.List;

import com.program.model.entity.Website;
import com.program.model.vo.*;
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



    @ApiOperation(value = "网站详情")
    @GetMapping(value = "/detail/{id}")
    public CommonResult detail(@PathVariable("id") Integer id){

        return CommonResult.build(websiteService.getById(id), CommonResultEnum.SUCCESS_QUERY);
    }

    @ApiOperation(value = "网站新增")
    @PostMapping("/add")
    public CommonResult add(@RequestBody Website website){
        websiteService.saveWebsite(website);
        return CommonResult.build(null, CommonResultEnum.SUCCESS_ADD);
    }

    @ApiOperation(value = "网站修改")
    @PostMapping("/edit")
    public CommonResult edit(@RequestBody Website website){

        return CommonResult.build(websiteService.updateById(website), CommonResultEnum.SUCCESS_QUERY);
    }

    @ApiOperation(value = "网站删除")
    @GetMapping("/remove/{ids}")
    public CommonResult remove(@PathVariable Integer[] ids){
        boolean b = websiteService.removeByIds(Arrays.asList(ids));
        return CommonResult.build(b, CommonResultEnum.SUCCESS_DELETE);
    }
}
