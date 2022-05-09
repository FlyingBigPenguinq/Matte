package com.syl.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.syl.annotation.Log;
import com.syl.dto.LoginUserDTO;
import com.syl.entity.SysUser;
import com.syl.enums.RedisCollectionEnum;
import com.syl.exception.RunException;
import com.syl.response.Response;
import com.syl.service.IAuthMenuService;
import com.syl.service.IAuthUserService;
import com.syl.util.RedisUtil;
import com.syl.util.SecurityUtil;
import com.syl.vo.SysMenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author Liu XiangLiang
 * @description 系统：鉴权
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "鉴权")
public class AuthController {

    private final IAuthUserService sysUserService;

    private final RedisUtil redisUtil;

    private final Producer producer;

    private final IAuthMenuService sysMenuService;

    @Log("登录")
    @ApiOperation("登录")
    @PostMapping("/login")
    public Response<Object> login(@Validated @RequestBody LoginUserDTO loginUserDTO) throws Exception {
        if (!loginUserDTO.getCode().equals(redisUtil.hget(RedisCollectionEnum.CAPTCHA_KEY.getKey(), loginUserDTO.getKey()))) {
            throw new RunException("验证码错误或过期");
        }
        redisUtil.hdel("captcha", loginUserDTO.getKey());
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(loginUserDTO, sysUser);
        return Response.success("登录成功").setData(sysUserService.login(sysUser));
    }

    @Log("获取用户信息")
    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public Response<Object> getUserInfo() {
        return Response.success().setData(sysUserService.getUserInfo(SecurityUtil.getCurrentUsername()));
    }

    @Log("登出")
    @ApiOperation("登出")
    @DeleteMapping("/logout")
    public Response<Object> logout() {
        sysUserService.logout();
        return Response.success("登出成功");
    }

    @ApiOperation("获取验证码")
    @GetMapping("/captcha")
    public Response<Object> captcha() throws IOException {
        String code = producer.createText();
        String key = IdUtil.simpleUUID();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        Base64.Encoder encoder = Base64.getEncoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + Arrays.toString(encoder.encode(outputStream.toByteArray()));
        // 存储到redis中
        redisUtil.hset(RedisCollectionEnum.CAPTCHA_KEY.getKey(), key, code, 60);
        log.info("验证码key:{},code:{}", key, code);
        return Response.success().setData(
                MapUtil.builder()
                        .put("key", key)
                        .put("url", base64Img)
                        .build()
        );
    }

    @ApiOperation("根据当前用户获取权限和菜单信息")
    @GetMapping("/getMenuByCurrentUser")
    public Response<Object> getMenuByCurrentUser() {
        List<SysMenuVO> sysMenuDtoList = sysMenuService.getMenuByCurrentUser(SecurityUtil.getCurrentUsername());
        return Response.success().setData(sysMenuDtoList);
    }

}
