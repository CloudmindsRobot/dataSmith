package com.cloudminds.data.smith.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.cloudminds.data.smith.dao.entity.SysUser;
import com.cloudminds.data.smith.dao.mapper.SysUserMapper;
import com.cloudminds.data.smith.dto.req.UserLoginReqDTO;
import com.cloudminds.data.smith.dto.resp.UserLoginInfoRespDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.service.AuthService;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户认证逻辑处理
 *
 * @author Tao.Liu
 * @date 2022/8/4 11:01
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserLoginInfoRespDTO userLogin(final UserLoginReqDTO loginReqDTO) {
        // 查询用户名
        SysUser sysUser = sysUserMapper.findByAccount(loginReqDTO.getAccount());
        Check.notNull(sysUser, "用户名或密码错误");
        final String encryptPassword = DigestUtil.md5Hex(loginReqDTO.getPassword());
        if (Strings.isNotEquals(encryptPassword, sysUser.getPassword())) {
            throw new ParameterException("用户名或密码错误");
        }

        final UserLoginInfoRespDTO respDTO = new UserLoginInfoRespDTO();
        respDTO.setUserName(sysUser.getName());
        respDTO.setLoginTime(System.currentTimeMillis());
        return respDTO;
    }

}
