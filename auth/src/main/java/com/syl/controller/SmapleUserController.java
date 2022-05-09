package com.syl.controller;

import com.syl.annotation.Log;
import com.syl.dto.LoginUserDTO;
import com.syl.entity.SysUser;
import com.syl.response.Response;
import com.syl.service.SpecialUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liu XiangLiang
 * @description: Sample Task Controller
 * @date 2022/5/2 下午1:20
 */
@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "单鉴权")
public class SmapleUserController {

    @Autowired
    private final SpecialUserService specialUserService;

    @Log("登录")
    @ApiOperation("登录")
    @PostMapping("/login")
    public Response<Object> login(@Validated @RequestBody LoginUserDTO loginUserDTO) throws Exception {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(loginUserDTO, sysUser);
        return Response.success("登录成功").setData(specialUserService.specialLogin(sysUser));
    }

}
