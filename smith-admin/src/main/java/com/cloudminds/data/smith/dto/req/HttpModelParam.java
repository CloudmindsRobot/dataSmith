package com.cloudminds.data.smith.dto.req;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * http模型参数
 *
 * @author Tao.Liu
 * @date 2022/7/6 15:18
 */
@Data
public class HttpModelParam {

    /**
     * 执行方法
     */
    private String method;

    /**
     * 请求内容
     */
    private Map<String, Object> body;

    public HttpModelParam() {
        this.method = "GET";
        this.body = new HashMap<>();
    }

}
