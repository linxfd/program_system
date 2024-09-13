package com.program.controller.common;

import com.program.model.dict.DictStatus;
import com.program.model.vo.CommonResult;
import com.program.model.vo.LikeVo;
import com.program.utils.JwtUtils;
import com.program.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author linxf
 * @date 2024/9/13
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/public/userInteract")
@Api(tags = "与用户交互的相关接口")
public class UserInteractController {

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "点赞或取消点赞")
    @GetMapping("/likeAndUnlike/{type}")
    public CommonResult likeAndUnlike(@PathVariable("type") Integer type, Integer itemId, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserInfoByToken(request).getId();
        if (DictStatus.COURSE.equals(type)) {
            boolean isLike = redisUtil.sHasKey("userInteraction:postLikes:course:" + itemId, userId);
            if (isLike) {
                redisUtil.setRemove("userInteraction:postLikes:course:" + itemId, userId);
            } else {
                redisUtil.sSet("userInteraction:postLikes:course:" + itemId, userId);
            }
            return CommonResult.<Boolean>builder()
                    .data(!isLike)
                    .message(isLike ? "取消点赞成功" : "点赞成功")
                    .build();
        }else {
            //还没有开发
            return CommonResult.<Boolean>builder()
                    .data(null)
                    .message("还没有开发")
                    .build();
        }
    }

    @ApiOperation(value = "查询用户点赞情况和总点赞量")
    @GetMapping("/queryLikes/{type}")
    public CommonResult queryLikes(@PathVariable("type") Integer type, Integer itemId, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserInfoByToken(request).getId();
        if (DictStatus.COURSE.equals(type)) {
            LikeVo likeVo = new LikeVo();
            likeVo.setIsLike(redisUtil.sHasKey("userInteraction:postLikes:course:" + itemId, userId));
            likeVo.setLikeCount(redisUtil.sGetSetSize("userInteraction:postLikes:course:" + itemId));
            return CommonResult.<LikeVo>builder()
                    .data(likeVo)
                    .message("查询成功")
                    .build();
        }else {
            return CommonResult.<Boolean>builder()
                    .data(null)
                    .message("还没有开发")
                    .build();
        }
    }


}
