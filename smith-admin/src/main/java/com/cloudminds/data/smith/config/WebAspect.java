package com.cloudminds.data.smith.config;

import cn.hutool.json.JSONUtil;
import com.cloudminds.data.smith.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamSource;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 统一记录接口日志切面类
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:09
 */
@Aspect
@Configuration
public class WebAspect {

    private static Logger log = LoggerFactory.getLogger(WebAspect.class);

    @Resource
    private HttpServletRequest request;

    /**
     * 超过1KB，则不输出响应
     */
    private final int MAX_IGNORE_RESP_BYTES = 1 * 1024;


    /**
     * 功能描述:  定义切面环绕通知得处理逻辑
     *
     * @param point
     * @return
     * @throws Throwable
     * @see
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object doAroundAdvice(final ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 请求参数
        final String params = this.formatParam(point), url = request.getRequestURI();

        log.info("===> Request Api [{}] params ---> {}", url, params);

        //执行方法逻辑
        final Object obj = point.proceed();

        final String result = JSONUtil.toJsonStr(obj);
        final String printResult = result.getBytes().length > MAX_IGNORE_RESP_BYTES ? "-" : result;
        log.info("===> Request Api [{}] cost: {}ms, response:{}, ", url, System.currentTimeMillis() - startTime, printResult);

        return obj;
    }


    /**
     * 获取格式化参数
     */
    private String formatParam(final JoinPoint joinPoint) {
        final Object[] paramValues = joinPoint.getArgs();
        if (paramValues == null || paramValues.length < 1) {
            return Strings.EMPTY;
        }
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        final Map<String, Object> paramMap = new HashMap<>(paramValues.length);
        for (int i = 0; i < paramValues.length; i++) {
            final Object paramValue = paramValues[i];
            if (!isPrint(paramValue)) {
                continue;
            }
            paramMap.put(paramNames[i], paramValue);
        }
        return JSONUtil.toJsonStr(paramMap);
    }

    /**
     * 是否打印参数
     *
     * @param target
     * @return
     */
    private boolean isPrint(Object target) {
        if (target == null) {
            return true;
        }
        if (target instanceof InputStreamSource || target instanceof ServletRequest || target instanceof ServletResponse) {
            return false;
        }
        return true;
    }

}
