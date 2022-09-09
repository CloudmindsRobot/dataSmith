package com.cloudminds.data.smith.exception;

/**
 * 错误码
 *
 * @author Tao.Liu
 * @date 2021/12/9 14:50
 */
public enum BaseErrorCodeEnum implements ICode {

    PARAM_MISS("parameter.miss", "参数缺失"),
    PARAM_ERROR("parameter.error", "参数错误"),
    PARAM_INVALID("parameter.invalid", "参数无效"),
    AUTH_TOKEN_INVALID("auth.token_invalid", "无效token"),
    AUTH_TOKEN_EXPIRED("auth.token_expired", "token已过期"),
    AUTH_NO_PERMISSION("auth.no_permission", "无权限访问"),
    SYSTEM_BUSINESS_ERROR("system.business_error", "系统业务出错"),
    SYSTEM_ERROR("system.error", "系统异常，请稍后再试"),
    SYSTEM_HYSTRIX_ERROR("system.hystrix_error", "内部服务调用出错"),
    SYSTEM_GATEWAY_ERROR("system.gateway_error", "网关错误"),
    SYSTEM_UNAVAILABLE("system.unavailable", "系统服务不可用"),
    EXTERNAL_SYSTEM_ERROR("external.system_error", "外部服务出错");
    /**
     * 错误码
     */
    private final String code;

    /**
     * 消息
     */
    private final String message;

    BaseErrorCodeEnum(final String code, final String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
