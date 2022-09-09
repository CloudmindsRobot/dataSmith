package com.cloudminds.data.smith.external.feishu.common.dto;

import lombok.Data;

import java.util.Objects;

/**
 * 飞书响应结构体
 *
 * @author Tao.Liu
 * @date 2022/6/29 14:27
 */
@Data
public class Resp<T> {

    public static final int SUCCESS_CODE = 0;

    /**
     * 错误码,0成功
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 获取成功响应结果
     *
     * @param resp
     * @param <T>
     * @return
     */
    public static <T> T getSuccessData(final Resp<T> resp) {
        if (isSuccess(resp)) {
            return resp.getData();
        }
        return null;
    }

    /**
     * 获取响应结果是否正常
     *
     * @param resp
     * @param <T>
     * @return
     */
    public static <T> boolean isSuccess(final Resp<T> resp) {
        if (resp == null || !Objects.equals(resp.getCode(), SUCCESS_CODE)) {
            return false;
        }
        return true;
    }

}
