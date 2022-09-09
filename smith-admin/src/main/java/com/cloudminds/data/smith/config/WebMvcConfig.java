package com.cloudminds.data.smith.config;

import com.cloudminds.data.smith.dto.resp.UserLoginInfoRespDTO;
import com.cloudminds.data.smith.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * mvc配置
 *
 * @author Tao.Liu
 * @date 2022/8/4 17:17
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注册自定义拦截器，添加拦截路径和排除拦截路径
     *
     * @param registry
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/doc.html", "/swagger-resources/**", "/webjars/**", "/v1/auth/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }


    /**
     * 登录拦截器
     */
    class LoginHandlerInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            final UserLoginInfoRespDTO loginInfoRespDTO = (UserLoginInfoRespDTO) request.getSession().getAttribute(AuthService.USER_LOGIN_SESSION_KEY);
            if (loginInfoRespDTO != null) {
                return true;
            }
            final String url = request.getRequestURI();
            log.info("===> Request Api [{}] Forbidden. The user is not logged in.", url);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

    }


}
