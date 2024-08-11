package com.program.controller.common;

import com.program.model.dict.DictTedisTime;
import com.program.model.vo.CommonResultEnum;
import com.program.service.UtilService;
import com.program.utils.CreateVerificationCode;
import com.program.utils.HttpUtils;
import com.program.utils.RedisUtil;
import com.program.model.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "工具类接口")
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilController {

    private final RedisUtil redisUtil;

    @Autowired
    private final UtilService utilService;
    @GetMapping("/getCodeImg")
    @ApiOperation(value = "获取验证码图片流")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "帮助前端生成验证码", dataType = "string", paramType = "query")
    })
    public void getIdentifyImage(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        //指定生成的响应图片
        response.setContentType("image/jpeg");
        CreateVerificationCode code = new CreateVerificationCode();
        BufferedImage image = code.getIdentifyImg();
        code.getG().dispose();
        //将图形验证码IO流传输至前端
        ImageIO.write(image, "JPEG", response.getOutputStream());
        redisUtil.set(id, code.getCode(), DictTedisTime.FIVE_TIME, TimeUnit.MINUTES);
    }

    @GetMapping("/getCode")
    @ApiOperation(value = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "帮助前端生成验证码", dataType = "string", paramType = "query")
    })
    public CommonResult<String> getCode(@RequestParam("id") String id) {
        return CommonResult.<String>builder()
                .data((String) redisUtil.get(id))
                .build();
    }
    //
    @ApiOperation(value = "发送手机验证码")
    @GetMapping(value = "/sendValidateCode/{phone}")
    public CommonResult sendValidateCode(@PathVariable String phone) {
        utilService.sendValidateCode(phone);
        return CommonResult.build(null, CommonResultEnum.SUCCESS);
    }

    @ApiOperation(value = "访问网址")
    @PostMapping("/fetchWebsite")
    public CommonResult fetchWebsiteTitle(@RequestBody String websiteUrl) {
        System.out.println("sss");
        // 解码 URL
        String decodedUrl = websiteUrl.substring(0, websiteUrl.length() - 1);
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.allorigins.win/get")
                .queryParam("url", decodedUrl)
                .build()
                .toUri();
        HttpResponse getResponse;
        Map<String, String> querys = new HashMap<>();
        querys.put("url", "https://www.baidu.com");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        String string = "";
        try {
            getResponse = HttpUtils.doGet("https://api.allorigins.win", "/get", "GET", headers, querys);
            if (getResponse != null) {
                string  = EntityUtils.toString(getResponse.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CommonResult.build(string, CommonResultEnum.SUCCESS);

    }
    private String decodeUrl(String encodedUrl) {
        try {
            // 使用 UTF-8 字符集解码
            return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            // 处理解码异常
            throw new RuntimeException("Failed to decode URL", e);
        }
    }

}
