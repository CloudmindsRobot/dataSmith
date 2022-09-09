package com.cloudminds.data.smith.exception;

/**
 * 服务码消息枚举
 *
 * @author Tao.Liu
 * @date 2021/9/27 15:19
 */
public enum ServiceCodeEnum implements IServiceCode {

    SUCCESS(0, "success"),
    SYSTEM_ERROR(4000, "系统错误，请稍后再试"),
    PARAM_ERROR(4001, "参数校验错误"),
    BUSINESS_ERROR(4002, "业务异常"),
    AUTH_ERROR(4003, "认证出错"),
    EXTERNAL_SYSTEM_ERROR(4004, "外部系统错误");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

    ServiceCodeEnum(final Integer code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
