package com.cloudminds.data.smith.controller;

import com.cloudminds.data.smith.dto.R;
import com.cloudminds.data.smith.dto.req.UserLoginReqDTO;
import com.cloudminds.data.smith.dto.resp.UserLoginInfoRespDTO;
import com.cloudminds.data.smith.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证
 *
 * @author Tao.Liu
 * @date 2022/8/4 10:50
 */
@Api(tags = "用户认证接口")
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @ApiOperation("用户登录")
    @PostMapping("userLogin")
    public R<UserLoginInfoRespDTO> userLogin(@RequestBody @Validated UserLoginReqDTO loginReqDTO, final HttpServletRequest request) {
        final UserLoginInfoRespDTO loginInfoRespDTO = authService.userLogin(loginReqDTO);
        request.getSession().setAttribute(AuthService.USER_LOGIN_SESSION_KEY, loginInfoRespDTO);
        return R.success(loginInfoRespDTO);
    }

    @ApiOperation("查询登录用户信息")
    @PostMapping("getLoginUserInfo")
    public R<UserLoginInfoRespDTO> getLoginUserInfo(final HttpServletRequest request) {
        final UserLoginInfoRespDTO loginInfoRespDTO = (UserLoginInfoRespDTO) request.getSession().getAttribute(AuthService.USER_LOGIN_SESSION_KEY);
        return R.success(loginInfoRespDTO);
    }

    @ApiOperation("用户登出")
    @PostMapping("logout")
    public R<Void> userLogout(final HttpServletRequest request) {
        request.getSession().removeAttribute(AuthService.USER_LOGIN_SESSION_KEY);
        return R.success();
    }


}
