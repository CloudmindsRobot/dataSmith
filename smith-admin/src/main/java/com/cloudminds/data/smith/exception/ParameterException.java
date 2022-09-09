package com.cloudminds.data.smith.exception;

/**
 * 参数异常
 *
 * @author Tao.Liu
 * @date 2021/12/9 14:31
 */
public class ParameterException extends BaseServiceException {

    private static final long serialVersionUID = -1191541920182895593L;

    public ParameterException(final String message) {
        super(ServiceCodeEnum.PARAM_ERROR, BaseErrorCodeEnum.PARAM_ERROR, message);
    }

    public ParameterException(final String message, final Object... args) {
        super(ServiceCodeEnum.PARAM_ERROR, BaseErrorCodeEnum.PARAM_ERROR, String.format(message, args));
    }

    public ParameterException(final ICode codeEnum) {
        super(ServiceCodeEnum.PARAM_ERROR, codeEnum);
    }

    public ParameterException(final BaseErrorCodeEnum codeEnum, final String message) {
        super(ServiceCodeEnum.PARAM_ERROR, codeEnum, message);
    }

    public ParameterException(final BaseErrorCodeEnum codeEnum, final String message, final Object... args) {
        super(ServiceCodeEnum.PARAM_ERROR, codeEnum, String.format(message, args));
    }

    public ParameterException(final ICode codeEnum, final Throwable cause) {
        super(ServiceCodeEnum.PARAM_ERROR, codeEnum, cause);
    }


}
