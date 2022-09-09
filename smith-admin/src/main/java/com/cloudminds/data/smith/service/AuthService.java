package com.cloudminds.data.smith.service;

import com.cloudminds.data.smith.dto.req.UserLoginReqDTO;
import com.cloudminds.data.smith.dto.resp.UserLoginInfoRespDTO;

/**
 * 用户认证逻辑处理
 *
 * @author Tao.Liu
 * @date 2022/8/4 10:53
 */
public interface AuthService {

    /**
     * 用户登录sessionKey
     */
    String USER_LOGIN_SESSION_KEY = "user_login_session_key";

    /**
     * 用户登录
     *
     * @param loginReqDTO
     * @return
     */
    UserLoginInfoRespDTO userLogin(UserLoginReqDTO loginReqDTO);

}
