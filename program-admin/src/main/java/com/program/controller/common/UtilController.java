package com.program.controller.common;

import com.program.exception.BusinessException;
import com.program.exception.CommonErrorCode;
import com.program.model.dict.DictTedisTime;
import com.program.model.vo.CommonResultEnum;
import com.program.service.UtilService;
import com.program.utils.CreateVerificationCode;
import com.program.utils.HtmlUtil;
import com.program.utils.RedisUtil;
import com.program.model.vo.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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

    @ApiOperation(value = "获得网页的标题和图标")
    @PostMapping("/documentInfo")
    public CommonResult fetchWebsiteTitle(@RequestBody String websiteUrl) {
        String url = "";
        Document document = null;
        try {
            // url解码
            String decode = URLDecoder.decode(websiteUrl, StandardCharsets.UTF_8.toString());
            url = decode.substring(0, decode.length() - 1);
            // Jsoup获取网页内容
            document = Jsoup.connect(url)
                    // 设置超时时间
                    .timeout(5000).get();
        } catch (Exception e) {
            if (e.getMessage().contains("connection refused")){
                throw new BusinessException(CommonErrorCode.E_500003);
            }else if (e.getMessage().contains("connect timed out")){
                throw new BusinessException(CommonErrorCode.E_500001);
            }else{
                throw new BusinessException(CommonErrorCode.UNKNOWN);
            }
        }

        // 获取网页标题、图标和描述
        String title = HtmlUtil.getTitle(document);
        List<String> imgs = HtmlUtil.getIcon(document, 20);
        String meta = HtmlUtil.getMeta(document);

        // 返回结果
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("title", title);
        response.put("icon", imgs);
        response.put("description", meta);

        return CommonResult.build(response, CommonResultEnum.SUCCESS_QUERY);
    }

}
