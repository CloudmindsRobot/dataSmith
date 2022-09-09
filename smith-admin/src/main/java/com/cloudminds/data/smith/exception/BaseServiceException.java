package com.cloudminds.data.smith.exception;

import lombok.Getter;

/**
 * 服务异常，主要用于接口响应码返回
 *
 * @author Tao.Liu
 * @date 2021/9/27 15:13
 */
@Getter
public class BaseServiceException extends BaseException {

    private static final long serialVersionUID = -1535885572500029548L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 基本错误码
     */
    private IServiceCode serviceCode;

    public BaseServiceException(final IServiceCode serviceCode, final ICode codeEnum, final String message) {
        super(message);
        this.serviceCode = serviceCode;
        this.code = codeEnum.getCode();
    }

    public BaseServiceException(final IServiceCode serviceCode, final ICode codeEnum) {
        this(serviceCode, codeEnum, codeEnum.getMessage());
    }

    public BaseServiceException(final IServiceCode serviceCode, final ICode codeEnum, final Throwable cause) {
        super(codeEnum.getMessage(), cause);
        this.serviceCode = serviceCode;
        this.code = codeEnum.getCode();
    }

}
